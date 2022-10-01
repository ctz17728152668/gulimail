package com.ctz.gulimail.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ctz.gulimail.product.dao.SkuInfoDao;
import com.ctz.gulimail.product.entity.SkuInfoEntity;
import com.ctz.gulimail.product.service.SkuInfoService;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        LambdaQueryWrapper<SkuInfoEntity> wrapper = new LambdaQueryWrapper<>();

        String key = (String) params.get("key");
        String catelogId = (String) params.get("catelogId");
        String brandId = (String) params.get("brandId");
        String max = (String) params.get("max");
        String min = (String) params.get("min");

        wrapper.and(!StringUtils.isEmpty(key),w->{
           w.eq(SkuInfoEntity::getSkuId,key).or().like(SkuInfoEntity::getSkuName,key);
        });
        wrapper.eq(!StringUtils.isEmpty(catelogId)&&!"0".equals(catelogId),SkuInfoEntity::getCatalogId,catelogId);
        wrapper.eq(!StringUtils.isEmpty(brandId)&&!"0".equals(brandId),SkuInfoEntity::getBrandId,brandId);
        wrapper.le(!StringUtils.isEmpty(max)&&!"0".equals(max),SkuInfoEntity::getPrice,max);
        wrapper.ge(!StringUtils.isEmpty(min)&&!"0".equals(min),SkuInfoEntity::getPrice,min);
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}