package com.ctz.gulimail.product.service;

import com.ctz.gulimail.product.entity.ProductAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 1
* @description 针对表【pms_product_attr_value(spu属性值)】的数据库操作Service
* @createDate 2022-09-29 11:14:58
*/
public interface ProductAttrValueService extends IService<ProductAttrValue> {

    List<ProductAttrValue> getValueBySpuId(Long spuId);

    void updateSpuAttr(Long spuId, List<ProductAttrValue> values);
}
