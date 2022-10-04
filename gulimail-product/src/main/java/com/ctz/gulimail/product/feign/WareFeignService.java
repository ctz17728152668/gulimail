package com.ctz.gulimail.product.feign;

import com.ctz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimall-ware")
public interface WareFeignService {

    @PostMapping("ware/waresku/hasStock")
    public R getSkuHasStockBySkuIds(@RequestBody List<Long> skuIds);

}
