package com.ctz.gulimail.product.dao;

import com.ctz.gulimail.product.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-12 15:50:15
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    List<String> getStringList(@Param("skuId") Long skuId);
}
