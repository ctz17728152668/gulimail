package com.ctz.gulimail.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;
import com.ctz.gulimail.product.entity.*;
import com.ctz.gulimail.product.service.*;
import com.ctz.gulimail.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ctz.gulimail.product.dao.SkuInfoDao;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    private SkuImagesService imagesService;

    @Autowired
    private SpuInfoDescService descService;

    @Autowired
    private AttrGroupService attrGroupService;


    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private ThreadPoolExecutor executor;


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

    /**
     * 根据skuId获取商品详细信息
     * @param skuId
     * @return
     */
    @Override
    public SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException {

        SkuItemVo skuItemVo = new SkuItemVo();

        CompletableFuture<SkuInfoEntity> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            //1.设置skuinfo信息
            SkuInfoEntity skuInfoEntity = getById(skuId);
            skuItemVo.setInfo(skuInfoEntity);
            return skuInfoEntity;
        }, executor);

        CompletableFuture<Void> descFuture = skuInfoFuture.thenAcceptAsync((res) -> {
            //2.设置spu描述信息
            SpuInfoDesc desc = descService.getOne(new LambdaQueryWrapper<SpuInfoDesc>().eq(SpuInfoDesc::getSpuId, res.getSpuId()));
            skuItemVo.setDesc(desc);
        }, executor);

        CompletableFuture<Void> groupFuture = skuInfoFuture.thenAcceptAsync((res) -> {
            //3.设置规格参数信息
            List<SkuItemVo.SpuItemAttrGroupVo> spuItemAttrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(res.getSpuId(), res.getCatalogId());
            skuItemVo.setGroupAttrs(spuItemAttrGroupVos);
        },executor);

        CompletableFuture<Void> saleAttrFuture = skuInfoFuture.thenAcceptAsync((res) -> {
            //4.设置销售属性信息
            List<SkuItemVo.SkuItemSaleAttrVo> skuItemSaleAttrVos = skuSaleAttrValueService.getSkuItemSaleAttrBySpuId(res.getSpuId());
            skuItemVo.setSaleAttr(skuItemSaleAttrVos);
        }, executor);

        CompletableFuture<Void> imagesFuture = CompletableFuture.runAsync(() -> {
            //5.设置图片信息
            List<SkuImages> list = imagesService.list(new LambdaQueryWrapper<SkuImages>().eq(SkuImages::getSkuId, skuId));
            skuItemVo.setImages(list);
        }, executor);

        CompletableFuture.allOf(descFuture,groupFuture,saleAttrFuture,imagesFuture).get();

        return skuItemVo;
    }

}