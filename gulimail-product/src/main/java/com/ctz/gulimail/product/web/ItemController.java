package com.ctz.gulimail.product.web;

import com.alibaba.fastjson.JSON;
import com.ctz.gulimail.product.service.SkuInfoService;
import com.ctz.gulimail.product.vo.SkuItemVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

@Controller
@Slf4j
public class ItemController {

    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String getSkuId(@PathVariable("skuId") Long skuId, Model model) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = skuInfoService.item(skuId);
        log.debug("{}", JSON.toJSONString(skuItemVo));
        model.addAttribute("item",skuItemVo);
        return "item1";
    }
}
