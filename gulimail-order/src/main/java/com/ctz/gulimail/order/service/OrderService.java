package com.ctz.gulimail.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctz.common.utils.PageUtils;
import com.ctz.gulimail.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-14 15:46:59
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

