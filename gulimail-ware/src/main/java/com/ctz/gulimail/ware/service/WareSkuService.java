package com.ctz.gulimail.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctz.common.to.SkuHasStockTo;
import com.ctz.common.utils.PageUtils;
import com.ctz.gulimail.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-14 15:51:47
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveOrUpdateSku(Long itemId);

    List<SkuHasStockTo> getSkuHasStockBySkuIds(List<Long> skuIds);
}

