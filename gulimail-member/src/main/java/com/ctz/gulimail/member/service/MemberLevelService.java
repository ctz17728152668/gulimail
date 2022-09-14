package com.ctz.gulimail.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctz.common.utils.PageUtils;
import com.ctz.gulimail.member.entity.MemberLevelEntity;

import java.util.Map;

/**
 * 会员等级
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-14 15:30:15
 */
public interface MemberLevelService extends IService<MemberLevelEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

