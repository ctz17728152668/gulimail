package com.ctz.gulimail.search.feign;

import com.ctz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-product")
public interface ProductFeignService {

    @RequestMapping("product/attr/info/{attrId}")
    public R attrInfo(@PathVariable("attrId") Long attrId);
}
