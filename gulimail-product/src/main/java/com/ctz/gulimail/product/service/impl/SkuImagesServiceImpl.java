package com.ctz.gulimail.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctz.gulimail.product.entity.SkuImages;
import com.ctz.gulimail.product.service.SkuImagesService;
import com.ctz.gulimail.product.dao.SkuImagesDao;
import com.ctz.gulimail.product.vo.Images;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 1
* @description 针对表【pms_sku_images(sku图片)】的数据库操作Service实现
* @createDate 2022-09-29 12:48:10
*/
@Service
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesDao, SkuImages>
    implements SkuImagesService{

    @Autowired
    private SkuImagesService skuImagesService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveImages(Long skuId, List<Images> images) {
        if(!CollectionUtils.isEmpty(images)){
            List<SkuImages> collect = images.stream()
                    .filter(image->!StringUtils.isEmpty(image.getImgUrl()))
                    .map((img -> {
                SkuImages skuImages = new SkuImages();
                skuImages.setSkuId(skuId);
                skuImages.setDefaultImg(img.getDefaultImg());
                skuImages.setImgUrl(img.getImgUrl());
                return skuImages;
            }))//仅需要图片路径不为空
                    .collect(Collectors.toList());

            skuImagesService.saveBatch(collect);
        }
    }
}




