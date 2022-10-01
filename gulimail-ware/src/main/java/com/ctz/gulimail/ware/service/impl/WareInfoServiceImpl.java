package com.ctz.gulimail.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;

import com.ctz.gulimail.ware.dao.WareInfoDao;
import com.ctz.gulimail.ware.entity.WareInfoEntity;
import com.ctz.gulimail.ware.service.WareInfoService;
import org.springframework.util.StringUtils;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                new QueryWrapper<WareInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        LambdaQueryWrapper<WareInfoEntity> wrapper = new LambdaQueryWrapper<>();

        String key = (String) params.get("key");

        wrapper.and(!StringUtils.isEmpty(key),(w)->{
            w.eq(WareInfoEntity::getId,key)
                    .or().like(WareInfoEntity::getName,key)
                    .or().like(WareInfoEntity::getAddress,key)
                    .or().like(WareInfoEntity::getAreacode,key);
        });
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}