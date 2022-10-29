package com.ctz.gulimail.auth.feign;

import com.ctz.common.utils.R;
import com.ctz.gulimail.auth.vo.SocialUser;
import com.ctz.gulimail.auth.vo.UserLoginVO;
import com.ctz.gulimail.auth.vo.UserRegistVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gulimall-member")
public interface MemberFeignService {

    @PostMapping("/member/member/regist")
    public R regist(@RequestBody UserRegistVO vo);

    @PostMapping("/member/member/login")
    public R login(@RequestBody UserLoginVO vo);

    @PostMapping("/member/member/oauth2/login")
    public R oauth2login(@RequestBody SocialUser vo);
}
