package com.ctz.gulimail.product.service;

import com.ctz.common.utils.PageUtils;
import com.ctz.gulimail.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author 1
* @description 针对表【pms_category_brand_relation(品牌分类关联)】的数据库操作Service
* @createDate 2022-09-23 16:33:46
*/
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {


    PageUtils queryPage(Map<String, Object> params);

    void saveDeatil(CategoryBrandRelationEntity categoryBrandRelationEntity);
}
