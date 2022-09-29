package com.ctz.gulimail.product.service;

import com.ctz.gulimail.product.entity.SkuImages;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ctz.gulimail.product.vo.Images;

import java.util.List;

/**
* @author 1
* @description 针对表【pms_sku_images(sku图片)】的数据库操作Service
* @createDate 2022-09-29 12:48:10
*/
public interface SkuImagesService extends IService<SkuImages> {

    void saveImages(Long skuId, List<Images> images);
}

