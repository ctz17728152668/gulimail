package com.ctz.gulimail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctz.common.utils.PageUtils;
import com.ctz.gulimail.product.entity.SkuInfoEntity;
import com.ctz.gulimail.product.vo.SkuItemVo;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-12 15:50:15
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageByCondition(Map<String, Object> params);

    SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException;
}

