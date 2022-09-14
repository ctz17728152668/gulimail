package com.ctz.gulimail.member.dao;

import com.ctz.gulimail.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-14 15:30:15
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
