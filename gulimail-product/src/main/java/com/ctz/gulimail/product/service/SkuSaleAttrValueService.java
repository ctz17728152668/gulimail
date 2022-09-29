package com.ctz.gulimail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctz.common.utils.PageUtils;
import com.ctz.gulimail.product.entity.SkuSaleAttrValueEntity;
import com.ctz.gulimail.product.vo.Attr;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-12 15:50:15
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(Long skuId, List<Attr> attr);
}

