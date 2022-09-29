package com.ctz.gulimail.coupon.feign;

import com.ctz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-member")
public interface F {

    @RequestMapping("/member/member/ll")
    public R ll();
}
