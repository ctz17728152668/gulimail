package com.ctz.gulimail.product.service.impl;

import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ctz.gulimail.product.dao.CategoryDao;
import com.ctz.gulimail.product.entity.CategoryEntity;
import com.ctz.gulimail.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listTree() {
        List<CategoryEntity> all =  baseMapper.selectList(null);
        List<CategoryEntity> collect = all.stream().filter(a -> a.getParentCid()==0)
                .map(categoryEntity -> {
                    List<CategoryEntity> children = findChildren(categoryEntity, all);
                    categoryEntity.setChildren(children);
                    return categoryEntity;
                })
                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .collect(Collectors.toList());

        return collect;
    }

    @Override
    public void removeMenusByIds(List<Long> asList) {
        //TODO 检查是否被其他依赖

        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] getPath(Long catelogId) {
        LinkedList<Long> longs = new LinkedList<>();
        while (catelogId!=0){
            longs.addFirst(catelogId);
            catelogId = this.getById(catelogId).getParentCid();
        }
        return longs.toArray(new Long[longs.size()]);
    }

    private List<CategoryEntity> findChildren(CategoryEntity category, List<CategoryEntity> all) {
        List<CategoryEntity> collect = all.stream().filter(categoryEntity -> category.getCatId().equals(categoryEntity.getParentCid()))
                .map(categoryEntity -> {
                    List<CategoryEntity> children = findChildren(categoryEntity, all);
                    categoryEntity.setChildren(children);
                    return categoryEntity;
                })
                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .collect(Collectors.toList());
        return collect;
    }

}