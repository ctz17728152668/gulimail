package com.ctz.gulimail.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;
import com.ctz.gulimail.product.entity.BrandEntity;
import com.ctz.gulimail.product.entity.CategoryBrandRelationEntity;
import com.ctz.gulimail.product.entity.CategoryEntity;
import com.ctz.gulimail.product.service.BrandService;
import com.ctz.gulimail.product.service.CategoryBrandRelationService;
import com.ctz.gulimail.product.dao.CategoryBrandRelationDao;
import com.ctz.gulimail.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
* @author 1
* @description 针对表【pms_category_brand_relation(品牌分类关联)】的数据库操作Service实现
* @createDate 2022-09-23 16:33:46
*/
@Service
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity>
    implements CategoryBrandRelationService{


    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDeatil(CategoryBrandRelationEntity categoryBrandRelationEntity) {
        Long brandId = categoryBrandRelationEntity.getBrandId();
        Long catelogId = categoryBrandRelationEntity.getCatelogId();
        BrandEntity brandEntity = brandService.getById(brandId);
        CategoryEntity categoryEntity = categoryService.getById(catelogId);
        categoryBrandRelationEntity.setBrandName(brandEntity.getName());
        categoryBrandRelationEntity.setCatelogName(categoryEntity.getName());
        save(categoryBrandRelationEntity);
    }

}




