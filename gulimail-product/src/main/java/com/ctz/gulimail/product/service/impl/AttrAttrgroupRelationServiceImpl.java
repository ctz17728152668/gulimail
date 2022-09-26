package com.ctz.gulimail.product.service.impl;

import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ctz.gulimail.product.dao.AttrAttrgroupRelationDao;
import com.ctz.gulimail.product.entity.AttrAttrgroupRelationEntity;
import com.ctz.gulimail.product.service.AttrAttrgroupRelationService;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void deleteRelations(AttrAttrgroupRelationEntity[] relations) {
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = Arrays.asList(relations);
        baseMapper.deleteBatchByAttrIds(attrAttrgroupRelationEntities);
    }

}