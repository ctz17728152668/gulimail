package com.ctz.gulimail.auth.controller;

import com.alibaba.fastjson.JSON;
import com.ctz.common.constant.AuthServerConstant;
import com.ctz.common.exception.BizCodeEnume;
import com.ctz.common.utils.R;
import com.ctz.common.vo.MemberRespVO;
import com.ctz.gulimail.auth.feign.MemberFeignService;
import com.ctz.gulimail.auth.feign.ThirdPartyFeignService;
import com.ctz.gulimail.auth.vo.UserLoginVO;
import com.ctz.gulimail.auth.vo.UserRegistVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class LoginController {

    @Autowired
    private ThirdPartyFeignService thirdPartyFeignService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MemberFeignService memberFeignService;

    @GetMapping("sms/sendCode")
    @ResponseBody
    public R sendCode(@RequestParam("phone") String phone){

        //TODO 1.接口防刷

        String redisCode = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if(!StringUtils.isEmpty(redisCode)&&System.currentTimeMillis()-Long.valueOf(redisCode.split("_")[1])<60000){
            //如果之前存在 判断是否存在 且判断是否在60秒以内 不能再发
            return R.error(BizCodeEnume.SMS_CODE_EXCEPTION.getCode(),BizCodeEnume.SMS_CODE_EXCEPTION.getMsg());
        }

        //2.验证码校验
        String code = UUID.randomUUID().toString().substring(0, 5);

        String key = code +"_" +System.currentTimeMillis();

        //将验证码加入redis 过期时间为10分钟
        stringRedisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX+phone,key,10, TimeUnit.MINUTES);

        thirdPartyFeignService.sendCode(phone,code);
        return R.ok();
    }


    @GetMapping("login.html")
    public String toLogin(HttpSession session){
        Object loginUser = session.getAttribute(AuthServerConstant.LOGIN_USER_PREFIX);
        if(loginUser==null){
            return "login";
        }
        return "redirect:http://gulimall.com";
    }


    /**
     * 重定向保留数据 使用session
     * TODO 分布式场景下session失效
     * @param registVO
     * @param result
     * @param redirectAttributes
     * @return
     */
    @PostMapping("register")
    public String regist(@Valid UserRegistVO registVO, BindingResult result, RedirectAttributes redirectAttributes){
        /**
         * model 重定向后消失 RedirectAttributes在重定向后仍保留
         * 当参数错误时 返回注册页面
         */
        if(result.hasErrors()){
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            redirectAttributes.addFlashAttribute("errors",errors);
            //请求转发 会导致页面携带数据 刷新页面时会重复提交
//            return "reg";
            //改为重定向 默认以当前服务器域名
//            return "redirect:/reg.html";
            return "redirect:http://auth.gulimall.com/reg.html";
        }

        //当参数正确时 远程调用注册用户方法
        String redisCode = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + registVO.getPhone());

        //先判断验证码是否正确
        if(!StringUtils.isEmpty(redisCode)&&redisCode.split("_")[0].equals(registVO.getCode())){
            //验证码正确
            //先删除redis中验证码
            stringRedisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + registVO.getPhone());

            R r = memberFeignService.regist(registVO);
            if(r.getCode()==0) {
                //远程调用成功则返回登录页

                return "redirect:http://auth.gulimall.com/login.html";
            }
            //否则返回注册页
            HashMap<String, String> errors = new HashMap<>();
            String msg = (String) r.get("msg");
            errors.put("msg",msg);
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.gulimall.com/reg.html";

        }
        //验证码不正确 返回注册页面 返回错误信息
        HashMap<String, String> errors = new HashMap<>();
        errors.put("code","验证码错误");
        redirectAttributes.addFlashAttribute("errors",errors);

        return "redirect:http://auth.gulimall.com/reg.html";
    }


    @PostMapping("login")
    public String login(UserLoginVO vo, RedirectAttributes redirectAttributes, HttpSession session){
        R login = memberFeignService.login(vo);
        if(login.getCode()==0){
            //成功登录
            String data = JSON.toJSONString(login.get("data"));
            MemberRespVO memberRespVO = JSON.parseObject(data, MemberRespVO.class);
            session.setAttribute(AuthServerConstant.LOGIN_USER_PREFIX,memberRespVO);
            return "redirect:http://gulimall.com";
        }
        HashMap<String, String> errors = new HashMap<>();
        String msg = (String) login.get("msg");
        errors.put("msg",msg);
        redirectAttributes.addFlashAttribute("errors",errors);
        return "redirect:http://auth.gulimall.com/login.html";
    }

}
