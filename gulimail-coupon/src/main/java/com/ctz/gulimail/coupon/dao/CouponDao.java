package com.ctz.gulimail.coupon.dao;

import com.ctz.gulimail.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-14 15:03:38
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
