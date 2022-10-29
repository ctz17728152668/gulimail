package com.ctz.gulimail.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;

import com.ctz.gulimail.member.dao.MemberLevelDao;
import com.ctz.gulimail.member.entity.MemberLevelEntity;
import com.ctz.gulimail.member.service.MemberLevelService;
import org.springframework.util.StringUtils;


@Service("memberLevelService")
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelDao, MemberLevelEntity> implements MemberLevelService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberLevelEntity> page = this.page(
                new Query<MemberLevelEntity>().getPage(params),
                new QueryWrapper<MemberLevelEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils getLevelList(Map<String, Object> params) {
        LambdaQueryWrapper<MemberLevelEntity> wrapper = new LambdaQueryWrapper<>();
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((queryWrapper)->{
                queryWrapper.eq(MemberLevelEntity::getId,key).or().like(MemberLevelEntity::getName,key);
            });
        }
        IPage<MemberLevelEntity> page = this.page(
                new Query<MemberLevelEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public Long getDefaultLevel() {
        MemberLevelEntity memberLevelEntity = getOne(new LambdaQueryWrapper<MemberLevelEntity>().eq(MemberLevelEntity::getDefaultStatus, 1));
        return memberLevelEntity.getId();
    }

}