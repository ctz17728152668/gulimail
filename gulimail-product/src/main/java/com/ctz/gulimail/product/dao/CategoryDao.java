package com.ctz.gulimail.product.dao;

import com.ctz.gulimail.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-12 15:50:15
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
