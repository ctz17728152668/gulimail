package com.ctz.gulimail.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ctz.gulimail.product.dao.AttrGroupDao;
import com.ctz.gulimail.product.entity.AttrGroupEntity;
import com.ctz.gulimail.product.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCatelogId(Map<String, Object> params, Long catelogId) {
        //若id为0 则查询全部
        IPage<AttrGroupEntity> page;
        if(catelogId==0){
            page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    new QueryWrapper<AttrGroupEntity>()
            );
        } else {
            //若id不为0 则仅查询该id对应属性
            LambdaQueryWrapper<AttrGroupEntity> wrapper = new LambdaQueryWrapper<>();
            //先查找指定id
            wrapper.eq(AttrGroupEntity::getCatelogId,catelogId);

            String key = (String) params.get("key");

            //若存在key 需要进行模糊查询 是否等于id 或模糊 name
            if (!StringUtils.isEmpty(key)) {
                wrapper.and((obj)->{
                    obj.eq(AttrGroupEntity::getAttrGroupId,key).or().like(AttrGroupEntity::getAttrGroupName,key);
                });
            }
            page = this.page(new Query<AttrGroupEntity>().getPage(params),wrapper);
        }
        return new PageUtils(page);
    }


}