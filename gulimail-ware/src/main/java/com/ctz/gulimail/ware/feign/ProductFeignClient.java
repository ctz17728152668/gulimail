package com.ctz.gulimail.ware.feign;

import com.ctz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("gulimall-product")
public interface ProductFeignClient {

    @GetMapping("product/skuinfo/name/{skuId}")
    public R getNameById(@PathVariable("skuId") Long skuId);
}
