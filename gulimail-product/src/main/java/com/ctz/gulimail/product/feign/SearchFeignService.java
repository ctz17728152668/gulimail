package com.ctz.gulimail.product.feign;

import com.ctz.common.to.es.SkuEsModel;
import com.ctz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;

@FeignClient("gulimall-search")
public interface SearchFeignService {

    @PostMapping("search/save/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList);
}
