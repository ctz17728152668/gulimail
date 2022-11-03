package com.ctz.gulimail.cart.feign;

import com.ctz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("gulimall-product")
public interface ProductFeignService {

    @GetMapping("product/skuinfo/info/{skuId}")
    public R getSkuInfo(@PathVariable("skuId") Long skuId);

    @GetMapping("product/skusaleattrvalue/stringlist/{skuId}")
    public List<String> getStringList(@PathVariable("skuId") Long skuId);

}
