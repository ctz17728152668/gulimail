package com.ctz.gulimail.order.dao;

import com.ctz.gulimail.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-14 15:46:59
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
