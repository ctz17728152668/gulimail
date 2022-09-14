package com.ctz.gulimail.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctz.common.utils.PageUtils;
import com.ctz.gulimail.ware.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-14 15:51:47
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

