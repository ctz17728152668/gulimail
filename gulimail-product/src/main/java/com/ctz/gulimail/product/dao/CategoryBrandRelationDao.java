package com.ctz.gulimail.product.dao;

import com.ctz.gulimail.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 1
* @description 针对表【pms_category_brand_relation(品牌分类关联)】的数据库操作Mapper
* @createDate 2022-09-23 16:33:46
* @Entity com.ctz.gulimail.product.entity.CategoryBrandRelation
*/
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

}




