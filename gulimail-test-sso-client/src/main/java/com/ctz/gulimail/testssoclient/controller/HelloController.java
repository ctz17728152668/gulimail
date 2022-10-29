package com.ctz.gulimail.testssoclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class HelloController {

    @Value("${sso.server.url}")
    public String ssoServerUrl;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 无需登录
     * @return
     */
    @GetMapping("hello")
    @ResponseBody
    public String hello(){
        return "hello";
    }

    @GetMapping("employees")
    public String employees(Model model, HttpSession session,@RequestParam(value = "token",required = false) String token){
        if(token!=null){
            //经过登录过程跳转过来
            String s = redisTemplate.opsForValue().get(token);
            ArrayList<String> list = new ArrayList<>();
            list.add("张三");
            list.add("李四");
            list.add("王五");
            model.addAttribute("emps",list);
            return "employees";
        }

        Object loginUser = session.getAttribute("loginUser");
        if(loginUser!=null){
            ArrayList<String> list = new ArrayList<>();
            list.add("张三");
            list.add("李四");
            list.add("王五");
            model.addAttribute("emps",list);
            return "employees";
        }
        //请求登录逻辑
        return "redirect:"+ssoServerUrl+"?url=http://client1.com:8081/employees";
    }
}
