package com.ctz.gulimail.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctz.common.utils.PageUtils;
import com.ctz.gulimail.coupon.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-14 15:03:38
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

