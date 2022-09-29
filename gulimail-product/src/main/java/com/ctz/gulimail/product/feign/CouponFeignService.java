package com.ctz.gulimail.product.feign;

import com.ctz.common.to.SkuReductionTo;
import com.ctz.common.to.SpuBoundTo;
import com.ctz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-coupon")
public interface CouponFeignService {



    @PostMapping("coupon/spubounds/save")
    //@RequiresPermissions("coupon:spubounds:save")
    public R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("coupon/skufullreduction/saveSkuReduction")
    public R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
