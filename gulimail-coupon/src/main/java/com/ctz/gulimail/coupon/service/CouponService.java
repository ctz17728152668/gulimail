package com.ctz.gulimail.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctz.common.utils.PageUtils;
import com.ctz.gulimail.coupon.entity.CouponEntity;

import java.util.Map;

/**
 * 优惠券信息
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-14 15:03:38
 */
public interface CouponService extends IService<CouponEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

