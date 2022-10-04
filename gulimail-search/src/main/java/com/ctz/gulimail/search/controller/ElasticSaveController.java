package com.ctz.gulimail.search.controller;

import com.ctz.common.exception.BizCodeEnume;
import com.ctz.common.to.es.SkuEsModel;
import com.ctz.common.utils.R;
import com.ctz.gulimail.search.service.ProductSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequestMapping("search/save")
@RestController
public class ElasticSaveController {

    @Autowired
    private ProductSaveService productSaveService;

    @PostMapping("product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList){
        boolean b = false;
        try {
            b = productSaveService.productStatusUp(skuEsModelList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return !b?R.ok():R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
    }
}
