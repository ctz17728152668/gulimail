package com.ctz.gulimail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctz.common.utils.PageUtils;
import com.ctz.gulimail.product.entity.CategoryEntity;
import com.ctz.gulimail.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-12 15:50:15
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listTree();

    void removeMenusByIds(List<Long> asList);

    Long[] getPath(Long catelogId);

    List<CategoryEntity> queryLevelOne();

    Map<String, List<Catelog2Vo>>  getcatalogJson();

    void updateCascade(CategoryEntity category);
}

