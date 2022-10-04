package com.ctz.gulimail.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctz.gulimail.product.entity.ProductAttrValue;
import com.ctz.gulimail.product.service.ProductAttrValueService;
import com.ctz.gulimail.product.dao.ProductAttrValueDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
* @author 1
* @description 针对表【pms_product_attr_value(spu属性值)】的数据库操作Service实现
* @createDate 2022-09-29 11:14:58
*/
@Service
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValue>
    implements ProductAttrValueService{

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Override
    public List<ProductAttrValue> getValueBySpuId(Long spuId) {
        List<ProductAttrValue> list = list(new LambdaQueryWrapper<ProductAttrValue>().eq(ProductAttrValue::getSpuId, spuId));
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSpuAttr(Long spuId, List<ProductAttrValue> values) {
        productAttrValueService.remove(new LambdaQueryWrapper<ProductAttrValue>().eq(ProductAttrValue::getSpuId,spuId));

        values.forEach(value->value.setSpuId(spuId));
        productAttrValueService.saveBatch(values);
    }
}




