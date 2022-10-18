package com.ctz.gulimail.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctz.common.constant.ProductConstant;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;
import com.ctz.gulimail.product.entity.AttrAttrgroupRelationEntity;
import com.ctz.gulimail.product.entity.AttrEntity;
import com.ctz.gulimail.product.entity.ProductAttrValue;
import com.ctz.gulimail.product.service.AttrAttrgroupRelationService;
import com.ctz.gulimail.product.service.AttrService;
import com.ctz.gulimail.product.service.ProductAttrValueService;
import com.ctz.gulimail.product.vo.AttrGroupRespVo;
import com.ctz.gulimail.product.vo.SkuItemVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ctz.gulimail.product.dao.AttrGroupDao;
import com.ctz.gulimail.product.entity.AttrGroupEntity;
import com.ctz.gulimail.product.service.AttrGroupService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrgroupRelationService relationService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCatelogId(Map<String, Object> params, Long catelogId) {

        String key = (String) params.get("key");
        LambdaQueryWrapper<AttrGroupEntity> wrapper = new LambdaQueryWrapper<>();

        //若存在key 需要进行模糊查询 是否等于id 或模糊 name
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((obj)->{
                obj.eq(AttrGroupEntity::getAttrGroupId,key).or().like(AttrGroupEntity::getAttrGroupName,key);
            });
        }
        //若id不为0 则需要查询id
        if(catelogId!=0){
            wrapper.eq(AttrGroupEntity::getCatelogId,catelogId);
        }
        //若id为0 则查询全部
        IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),wrapper);
        return new PageUtils(page);
    }

    @Override
    public List<AttrEntity> getAttrByGroupId(Long attrgroupId) {
        List<Long> attrIds = relationService.list(
                new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrgroupId)
        ).stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        List<AttrEntity> list  = null;
        if(!CollectionUtils.isEmpty(attrIds)){
            list = (List<AttrEntity>) attrService.listByIds(attrIds);
        }

        return list;
    }

    @Override
    public List<AttrGroupRespVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        List<AttrGroupRespVo> attrGroupRespVos = this.list(new LambdaQueryWrapper<AttrGroupEntity>()
                .eq(AttrGroupEntity::getCatelogId, catelogId))
                .stream()
                .map((groupEntity) -> {
                    AttrGroupRespVo attrGroupRespVo = new AttrGroupRespVo();
                    BeanUtils.copyProperties(groupEntity, attrGroupRespVo);
                    return attrGroupRespVo;
                }).collect(Collectors.toList());

        attrGroupRespVos.forEach((attrGroupRespVo)->{
            List<Long> attrIds = relationService.list(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupRespVo.getAttrGroupId()))
                    .stream()
                    .map(AttrAttrgroupRelationEntity::getAttrId)
                    .collect(Collectors.toList());

            List<AttrEntity> collect = (List<AttrEntity>) attrService.listByIds(attrIds);

            attrGroupRespVo.setAttrs(collect);
        });
        return attrGroupRespVos;
    }

    @Override
    public List<SkuItemVo.SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId) {

        List<AttrGroupEntity> groupEntityList = list(new LambdaQueryWrapper<AttrGroupEntity>().eq(AttrGroupEntity::getCatelogId, catalogId));

        List<ProductAttrValue> productAttrValues = productAttrValueService.getValueBySpuId(spuId);

        Map<Long, ProductAttrValue> attrValueMap = productAttrValues.stream().collect(Collectors.toMap(ProductAttrValue::getAttrId, item -> item));

        Map<Long, List<AttrAttrgroupRelationEntity>> groupIdMap = productAttrValues.stream().map(ProductAttrValue::getAttrId).map(item -> {
            return relationService.getOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, item));
        }).collect(Collectors.groupingBy(AttrAttrgroupRelationEntity::getAttrGroupId));

        List<SkuItemVo.SpuItemAttrGroupVo> result = groupEntityList.stream().map(item -> {
            SkuItemVo.SpuItemAttrGroupVo spuItemAttrGroupVo = new SkuItemVo.SpuItemAttrGroupVo();
            spuItemAttrGroupVo.setGroupName(item.getAttrGroupName());
            List<SkuItemVo.SpuBaseAttrVo> spuBaseAttrVos = groupIdMap.get(item.getAttrGroupId()).stream().map(AttrAttrgroupRelationEntity::getAttrId)
                    .map(attrId -> {
                        SkuItemVo.SpuBaseAttrVo spuBaseAttrVo = new SkuItemVo.SpuBaseAttrVo();
                        ProductAttrValue productAttrValue = attrValueMap.get(attrId);
                        spuBaseAttrVo.setAttrName(productAttrValue.getAttrName());
                        spuBaseAttrVo.setAttrValue(productAttrValue.getAttrValue());
                        return spuBaseAttrVo;
                    }).collect(Collectors.toList());
            spuItemAttrGroupVo.setAttrs(spuBaseAttrVos);
            return spuItemAttrGroupVo;
        }).collect(Collectors.toList());
        return result;
    }


}