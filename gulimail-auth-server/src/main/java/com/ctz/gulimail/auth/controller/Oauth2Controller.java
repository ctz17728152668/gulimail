package com.ctz.gulimail.auth.controller;

import com.alibaba.fastjson.JSON;
import com.ctz.common.constant.AuthServerConstant;
import com.ctz.common.utils.R;
import com.ctz.common.vo.MemberRespVO;
import com.ctz.gulimail.auth.feign.MemberFeignService;
import com.ctz.gulimail.auth.utils.HttpUtils;
import com.ctz.gulimail.auth.vo.SocialUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;

@Controller
@RequestMapping("oauth2.0")
@Slf4j
public class Oauth2Controller {

    @Autowired
    private MemberFeignService memberFeignService;

    @GetMapping("gitee/success")
    public String gitee(@RequestParam("code") String code, HttpSession session){
        System.out.println(code);
        HashMap<String, String> map = new HashMap<>();
        map.put("grant_type","authorization_code");
        map.put("code",code);
        map.put("client_id","63e8497cdf152f2ac9c23f51a9a08532995730b60468694c792a3847d4cd8322");
        map.put("redirect_uri","http://auth.gulimall.com/oauth2.0/gitee/success");
        map.put("client_secret","ff4c4c53b265e36fd5c57353965746ceaef7f79cffeaef67accb38ecf8063c1a");
        try {
            HttpResponse post = HttpUtils.doPost("https://gitee.com", "/oauth/token", "post", Collections.emptyMap(), Collections.emptyMap(), map);
            String json = EntityUtils.toString(post.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);

            //调用远程登录注册方法
            R r = memberFeignService.oauth2login(socialUser);

            if(r.getCode()==0){
                //获取成功则返回首页
                String data = JSON.toJSONString(r.get("data"));
                MemberRespVO memberRespVO = JSON.parseObject(data, MemberRespVO.class);
                session.setAttribute(AuthServerConstant.LOGIN_USER_PREFIX,memberRespVO);
                //TODO 保存session时 domain作用域设置为子域
                log.info("登录成功:{}",memberRespVO);
                return "redirect:http://gulimall.com";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //否则返回登录页
        return "redirect:http://auth.gulimall.com/login.html";
    }
}
