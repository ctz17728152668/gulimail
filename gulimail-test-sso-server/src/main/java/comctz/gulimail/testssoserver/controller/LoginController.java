package comctz.gulimail.testssoserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class LoginController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("login.html")
    public String login(String url, Model model){
        model.addAttribute("url",url);
        return "login";
    }

    @PostMapping("doLogin")
    public String ll(String username,String password,String url){
        if(!StringUtils.isEmpty(username)&&!StringUtils.isEmpty(password)){
            String token = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(token,username);
            return "redirect:"+url+"?token="+token;
        }
        //登录完成后 跳转回到当前页面
        return "login";
    }
}
