package com.ctz.gulimail.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;
import com.ctz.gulimail.product.service.CategoryBrandRelationService;
import com.ctz.gulimail.product.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ctz.gulimail.product.dao.CategoryDao;
import com.ctz.gulimail.product.entity.CategoryEntity;
import com.ctz.gulimail.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CategoryBrandRelationService relationService;

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

    @Cacheable(value = {"category"},key = "#root.methodName",sync = true)
    @Override
    public List<CategoryEntity> queryLevelOne() {
        List<CategoryEntity> list = list(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getParentCid, 0));
        return list;
    }

    /**
     * 查询所有二三级分类数据
     * @return
     */
    @Override
    public Map<String, List<Catelog2Vo>> getcatalogJson(){
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        //若缓存种存在 则直接返回
        if(!StringUtils.isEmpty(catalogJson)){
            Map<String, List<Catelog2Vo>> stringListMap = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {});
            return stringListMap;
        }
        //缓存中不存在 需要查询数据库 防止缓存击穿 访问数据库需要加锁
        Map<String, List<Catelog2Vo>> stringListMap = null;
        RLock lock = redissonClient.getLock("catalogJson-lock");
        lock.lock();
        try {
            stringListMap = getcatalogJsonFromDb();
        } finally {
            lock.unlock();
        }
        return stringListMap;
    }

    @Caching(evict = {
            @CacheEvict(value = "category",key = "'queryLevelOne'"),
            @CacheEvict(value = "category",key = "'catalogJson'")
    })
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        relationService.updateCategory(category.getCatId(),category.getName());
        //修改缓存中的数据
    }


    public Map<String, List<Catelog2Vo>>  getcatalogJsonFromDb() {
        //获取到分类信息
        List<CategoryEntity> list = list();
        List<CategoryEntity> levelOne = list.stream().filter(item -> item.getParentCid() == 0).collect(Collectors.toList());
        Map<String, List<Catelog2Vo>> collect = levelOne.stream().collect(Collectors.toMap(categoryEntity -> categoryEntity.getCatId().toString(), (one) -> {
            List<Catelog2Vo> twoList = list.stream().filter(two -> two.getParentCid().equals(one.getCatId()))
                    .map(two -> {
                        Catelog2Vo catelog2Vo = new Catelog2Vo();
                        catelog2Vo.setId(two.getCatId().toString());
                        catelog2Vo.setName(two.getName());
                        catelog2Vo.setCatalog1Id(two.getParentCid().toString());

                        List<Catelog2Vo.Catelog3Vo> threeList = list.stream().filter(three -> three.getParentCid().equals(two.getCatId()))
                                .map(three -> {
                                    Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo();
                                    catelog3Vo.setCatalog2Id(two.getCatId().toString());
                                    catelog3Vo.setId(three.getCatId().toString());
                                    catelog3Vo.setName(three.getName());
                                    return catelog3Vo;
                                }).collect(Collectors.toList());

                        catelog2Vo.setCatalog3List(threeList);

                        return catelog2Vo;

                    }).collect(Collectors.toList());
            return twoList;
        }));
        String catalogJson = JSON.toJSONString(collect);
        //并将分类信息加入缓存 保证原子性
        redisTemplate.opsForValue().set("catalogJson",catalogJson);
        return collect;
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