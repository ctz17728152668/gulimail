package com.ctz.gulimail.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;
import com.ctz.gulimail.product.entity.SkuInfoEntity;
import com.ctz.gulimail.product.service.SkuInfoService;
import com.ctz.gulimail.product.vo.Attr;
import com.ctz.gulimail.product.vo.SkuItemVo;
import jodd.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ctz.gulimail.product.dao.SkuSaleAttrValueDao;
import com.ctz.gulimail.product.entity.SkuSaleAttrValueEntity;
import com.ctz.gulimail.product.service.SkuSaleAttrValueService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private SkuInfoService skuInfoService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAttr(Long skuId, List<Attr> attr) {
        if(!CollectionUtils.isEmpty(attr)){
            List<SkuSaleAttrValueEntity> collect = attr.stream().map((a -> {
                SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                BeanUtils.copyProperties(a, skuSaleAttrValueEntity);
                skuSaleAttrValueEntity.setSkuId(skuId);
                return skuSaleAttrValueEntity;
            })).collect(Collectors.toList());

            skuSaleAttrValueService.saveBatch(collect);
        }
    }

    @Override
    public List<SkuItemVo.SkuItemSaleAttrVo> getSkuItemSaleAttrBySpuId(Long spuId) {
        List<Long> collect = skuInfoService.list(new LambdaQueryWrapper<SkuInfoEntity>().eq(SkuInfoEntity::getSpuId, spuId)).stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        List<SkuSaleAttrValueEntity> list = skuSaleAttrValueService.list(new LambdaQueryWrapper<SkuSaleAttrValueEntity>().in(SkuSaleAttrValueEntity::getSkuId, collect));

        Map<Long, List<SkuSaleAttrValueEntity>> map = list.stream().collect(Collectors.groupingBy(SkuSaleAttrValueEntity::getAttrId));
        Map<String, List<SkuSaleAttrValueEntity>> valueMap = list.stream().collect(Collectors.groupingBy(SkuSaleAttrValueEntity::getAttrValue));

        ArrayList<SkuItemVo.SkuItemSaleAttrVo> skuItemSaleAttrVos = new ArrayList<>();
        for (Map.Entry<Long, List<SkuSaleAttrValueEntity>> entry : map.entrySet()) {

            SkuItemVo.SkuItemSaleAttrVo skuItemSaleAttrVo = new SkuItemVo.SkuItemSaleAttrVo();

            skuItemSaleAttrVo.setAttrId(entry.getKey());
            String attrName = entry.getValue().get(0).getAttrName();
            skuItemSaleAttrVo.setAttrName(attrName);

            List<SkuItemVo.AttrValueWithSkuIdVo> attrValueWithSkuIdVos = entry.getValue().stream().map(item -> {
                SkuItemVo.AttrValueWithSkuIdVo attrValueWithSkuIdVo = new SkuItemVo.AttrValueWithSkuIdVo();
                attrValueWithSkuIdVo.setAttrValue(item.getAttrValue());
                List<Long> longList = valueMap.get(item.getAttrValue()).stream().map(SkuSaleAttrValueEntity::getSkuId).collect(Collectors.toList());
                String join = StringUtil.join(longList, ",");
                attrValueWithSkuIdVo.setSkuIds(join);
                return attrValueWithSkuIdVo;
            }).distinct().collect(Collectors.toList());

            skuItemSaleAttrVo.setAttrValues(attrValueWithSkuIdVos);
            skuItemSaleAttrVos.add(skuItemSaleAttrVo);

        }
        return skuItemSaleAttrVos;
    }

    @Override
    public List<String> getStringList(Long skuId) {
        return baseMapper.getStringList(skuId);
    }

}