package com.ctz.gulimail.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctz.common.constant.WareConstant;
import com.ctz.common.to.SkuHasStockTo;
import com.ctz.common.utils.R;
import com.ctz.gulimail.ware.entity.PurchaseDetailEntity;
import com.ctz.gulimail.ware.feign.ProductFeignClient;
import com.ctz.gulimail.ware.service.PurchaseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;

import com.ctz.gulimail.ware.dao.WareSkuDao;
import com.ctz.gulimail.ware.entity.WareSkuEntity;
import com.ctz.gulimail.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    private WareSkuService wareSkuService;

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WareSkuEntity> wrapper = new LambdaQueryWrapper<>();

        String wareId = (String) params.get("wareId");
        String skuId = (String) params.get("skuId");

        wrapper.eq(!StringUtils.isEmpty(wareId),WareSkuEntity::getWareId,wareId);
        wrapper.eq(!StringUtils.isEmpty(skuId),WareSkuEntity::getSkuId,skuId);

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    /**
     * 提交采购需求 若不存在则创建 存在则添加
     * @param itemId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateSku(Long itemId) {
        PurchaseDetailEntity detail = purchaseDetailService.getById(itemId);
        Long skuId = detail.getSkuId();
        Long wareId = detail.getWareId();
        LambdaQueryWrapper<WareSkuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WareSkuEntity::getSkuId,skuId).eq(WareSkuEntity::getWareId,wareId);
        //根据sku 和仓库id 判断是否存在
        WareSkuEntity wareSkuEntity = wareSkuService.getOne(wrapper);

        //库存已经存在 则只更新库存数量
        if(wareSkuEntity!=null){
            WareSkuEntity temp = new WareSkuEntity();
            temp.setId(wareSkuEntity.getId());
            temp.setStock(wareSkuEntity.getStock()+detail.getSkuNum());
            wareSkuService.updateById(temp);
        } else {
            //库存不存在 需要创建新的
            wareSkuEntity = new WareSkuEntity();
            R r = productFeignClient.getNameById(skuId);
            String skuName = (String) r.get("skuName");
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(detail.getSkuNum());
            wareSkuEntity.setSkuName(skuName);
            wareSkuEntity.setStockLocked(WareConstant.SkuStockLockStatus.UNLOCKED.getCode());
            wareSkuService.save(wareSkuEntity);
        }
    }

    @Override
    public List<SkuHasStockTo> getSkuHasStockBySkuIds(List<Long> skuIds) {
        List<SkuHasStockTo> collect = skuIds.stream().map((id) -> {
            SkuHasStockTo skuHasStockTo = new SkuHasStockTo();
            skuHasStockTo.setSkuId(id);
            Long count = baseMapper.getStockBySkuId(id);
            skuHasStockTo.setHasStock(count==null?false:count>0);
            return skuHasStockTo;
        }).collect(Collectors.toList());
        return collect;
    }

}