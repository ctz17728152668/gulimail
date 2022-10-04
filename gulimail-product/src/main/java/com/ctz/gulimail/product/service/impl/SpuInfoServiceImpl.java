package com.ctz.gulimail.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctz.common.constant.ProductConstant;
import com.ctz.common.to.SkuHasStockTo;
import com.ctz.common.to.SkuReductionTo;
import com.ctz.common.to.SpuBoundTo;
import com.ctz.common.to.es.SkuEsModel;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;
import com.ctz.common.utils.R;
import com.ctz.gulimail.product.entity.*;
import com.ctz.gulimail.product.feign.CouponFeignService;
import com.ctz.gulimail.product.feign.SearchFeignService;
import com.ctz.gulimail.product.feign.WareFeignService;
import com.ctz.gulimail.product.service.*;
import com.ctz.gulimail.product.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ctz.gulimail.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {


    @Autowired
    private SpuInfoService spuInfoService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WareFeignService wareFeignService;

    @Autowired
    private SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {
        //1.保存spu基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo,spuInfoEntity);
        spuInfoService.save(spuInfoEntity);

        //2.保存spu 图片信息 pms_spu_images
        spuImagesService.saveImagesBatch(spuInfoEntity.getId(),spuSaveVo.getImages());

        //3.保存spu 描述信息 pms_spu_info_desc
        spuInfoDescService.saveDescBatch(spuInfoEntity.getId(),spuSaveVo.getDecript());

        //4. 插入规格参数信息 pms_product_attr_value
        List<ProductAttrValue> productAttrValueList = spuSaveVo.getBaseAttrs().stream().map((baseAttr) -> {
            ProductAttrValue productAttrValue = new ProductAttrValue();
            productAttrValue.setSpuId(spuInfoEntity.getId());
            productAttrValue.setAttrValue(baseAttr.getAttrValues());
            productAttrValue.setAttrId(baseAttr.getAttrId());
            AttrEntity attr = attrService.getById(baseAttr.getAttrId());
            productAttrValue.setAttrName(attr.getAttrName());
            productAttrValue.setQuickShow(baseAttr.getShowDesc());
            return productAttrValue;
        }).collect(Collectors.toList());
        productAttrValueService.saveBatch(productAttrValueList);


        //5. 插入会员积分 gulimall-sms->sms_spu_bounds
        Bounds bounds = spuSaveVo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if(r.getCode()!=0){
            log.error("远程保存积分信息失败");
        }

        //6.插入skus
        List<Skus> skus = spuSaveVo.getSkus();
        if(!CollectionUtils.isEmpty(skus)){
            skus.forEach((sku)->{

                //6.1基本信息 pms_sku_info
                String defaultImg = "";
                for (Images image : sku.getImages()) {
                    if(image.getDefaultImg()==1){
                        defaultImg = image.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku,skuInfoEntity);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoEntity.setPrice(sku.getPrice());
                skuInfoService.save(skuInfoEntity);

                Long skuId = skuInfoEntity.getSkuId();

                //6.2插入sku图片信息 pms_sku_images
                List<Images> images = sku.getImages();
                skuImagesService.saveImages(skuId,images);


                //6.3插入sku销售属性信息 pms_sku_sale_attr_value
                List<Attr> attr = sku.getAttr();
                skuSaleAttrValueService.saveAttr(skuId,attr);

                //6.4gulimall-sms->sms_sku_ladder/sms_sku_full_reduction优惠满减
                if(sku.getFullCount()>0||sku.getFullPrice().compareTo(BigDecimal.ZERO)>0){
                    SkuReductionTo skuReductionTo = new SkuReductionTo();
                    BeanUtils.copyProperties(sku,skuReductionTo);
                    skuReductionTo.setSkuId(skuInfoEntity.getSkuId());
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if(r1.getCode()!=0){
                        log.error("远程保存积分信息失败");
                    }
                }
            });
        }



    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        LambdaQueryWrapper<SpuInfoEntity> wrapper = new LambdaQueryWrapper<>();

        String key = (String) params.get("key");
        String catelogId = (String) params.get("catelogId");
        String brandId = (String) params.get("brandId");
        String status = (String) params.get("status");

        wrapper.and(!StringUtils.isEmpty(key),(w)->{
            w.eq(SpuInfoEntity::getId,key).or().like(SpuInfoEntity::getSpuName,key);
        });
        wrapper.eq(!StringUtils.isEmpty(catelogId)&&!catelogId.equals("0"),SpuInfoEntity::getCatalogId,catelogId);
        wrapper.eq(!StringUtils.isEmpty(brandId)&&!brandId.equals("0"),SpuInfoEntity::getBrandId,brandId);
        wrapper.eq(!StringUtils.isEmpty(status),SpuInfoEntity::getPublishStatus,status);

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    /**
     * 商品上架
     * @param spuId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void up(Long spuId) {
        //查询所有sku 封装为SkuEsModel
        List<SkuInfoEntity> skuInfoEntities = skuInfoService.list(new LambdaQueryWrapper<SkuInfoEntity>().eq(SkuInfoEntity::getSpuId, spuId));

        List<Long> skuIds = skuInfoEntities.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());

        SpuInfoEntity spuInfo = spuInfoService.getById(spuId);
        BrandEntity brand = brandService.getById(spuInfo.getBrandId());
        CategoryEntity category = categoryService.getById(spuInfo.getCatalogId());

        //所有的规格参数
        List<ProductAttrValue> values = productAttrValueService.getValueBySpuId(spuId);
        List<Long> attrIds = values.stream().map(ProductAttrValue::getAttrId).collect(Collectors.toList());
        //找出显示的参数
        List<Long> searchAttrByIds = attrService.getSearchAttrByIds(attrIds);
        HashSet<Long> longs = new HashSet<>(searchAttrByIds);
        List<SkuEsModel.attr> attrs = values.stream().filter((item) -> longs.contains(item.getAttrId())).map((item) -> {
            SkuEsModel.attr attr = new SkuEsModel.attr();
            BeanUtils.copyProperties(item, attr);
            return attr;
        }).collect(Collectors.toList());

        //查询库存
        Map<Long, Boolean> hasStock = null;
        try {
            R r = wareFeignService.getSkuHasStockBySkuIds(skuIds);
            String data = JSON.toJSONString(r.get("data"));
            List<SkuHasStockTo> tos = JSON.parseArray(data, SkuHasStockTo.class);
            hasStock = tos.stream().collect(Collectors.toMap(SkuHasStockTo::getSkuId, SkuHasStockTo::getHasStock));
        } catch (Exception e) {
            log.error("库存查询信息失败,原因：{}",e);
        }


        Map<Long, Boolean> finalHasStock = hasStock;
        List<SkuEsModel> collect = skuInfoEntities.stream().map(skuInfoEntity -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(skuInfoEntity, skuEsModel);
            skuEsModel.setSkuPrice(skuInfoEntity.getPrice());
            skuEsModel.setSkuImg(skuInfoEntity.getSkuDefaultImg());
            //设置库存
            if(finalHasStock == null){
                skuEsModel.setHasStock(true);
            } else {
                skuEsModel.setHasStock(finalHasStock.get(skuEsModel.getSkuId()));
            }
            //热度评分设为0
            skuEsModel.setHotScore(0L);
            //brandName,brandImg,catalogName
            skuEsModel.setBrandName(brand.getName());
            skuEsModel.setBrandImg(brand.getLogo());
            skuEsModel.setCatalogName(category.getName());
            //attrs
            skuEsModel.setAttrs(attrs);
            return skuEsModel;
        }).collect(Collectors.toList());

        //发送给es检索服务
        R r = searchFeignService.productStatusUp(collect);
        if(r.getCode() == 0){
            //远程调用成功
            SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
            spuInfoEntity.setId(spuId);
            spuInfoEntity.setPublishStatus(ProductConstant.StatusEnum.SPU_UP.getCode());
            spuInfoService.updateById(spuInfoEntity);
        } else {
            log.error("es提交数据失败");
        }

    }

}