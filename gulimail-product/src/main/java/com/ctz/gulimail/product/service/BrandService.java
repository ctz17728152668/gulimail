package com.ctz.gulimail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctz.common.utils.PageUtils;
import com.ctz.gulimail.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-12 15:50:15
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

