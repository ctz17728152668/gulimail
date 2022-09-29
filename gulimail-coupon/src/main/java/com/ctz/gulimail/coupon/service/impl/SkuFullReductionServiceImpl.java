package com.ctz.gulimail.coupon.service.impl;

import com.ctz.common.to.MemberPrice;
import com.ctz.common.to.SkuReductionTo;
import com.ctz.gulimail.coupon.entity.MemberPriceEntity;
import com.ctz.gulimail.coupon.entity.SkuLadderEntity;
import com.ctz.gulimail.coupon.service.MemberPriceService;
import com.ctz.gulimail.coupon.service.SkuLadderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;

import com.ctz.gulimail.coupon.dao.SkuFullReductionDao;
import com.ctz.gulimail.coupon.entity.SkuFullReductionEntity;
import com.ctz.gulimail.coupon.service.SkuFullReductionService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {


    @Autowired
    private SkuLadderService skuLadderService;

    @Autowired
    private SkuFullReductionService skuFullReductionService;

    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        //6.1满几件打几折优惠 阶梯价格
        if(skuReductionTo.getFullCount()>0){
            SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
            skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
            skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
            skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
            skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
            skuLadderService.save(skuLadderEntity);
        }
        //金额满多少减多少
        if(skuReductionTo.getFullPrice().compareTo(BigDecimal.ZERO)>0){
            SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
            skuFullReductionEntity.setSkuId(skuReductionTo.getSkuId());
            skuFullReductionEntity.setFullPrice(skuReductionTo.getFullPrice());
            skuFullReductionEntity.setReducePrice(skuReductionTo.getReducePrice());
            skuFullReductionEntity.setAddOther(skuReductionTo.getPriceStatus());
            skuFullReductionService.save(skuFullReductionEntity);
        }


        //会员价格
        List<MemberPrice> memberPrice = skuReductionTo.getMemberPrice();
        if(!CollectionUtils.isEmpty(memberPrice)){
            List<MemberPriceEntity> collect = memberPrice.stream().map((price) -> {
                MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
                memberPriceEntity.setMemberLevelId(price.getId());
                memberPriceEntity.setMemberPrice(price.getPrice());
                memberPriceEntity.setAddOther(1);
                return memberPriceEntity;
            }).filter(price -> !price.getMemberPrice().equals(BigDecimal.ZERO)).collect(Collectors.toList());

            memberPriceService.saveBatch(collect);
        }
    }

}