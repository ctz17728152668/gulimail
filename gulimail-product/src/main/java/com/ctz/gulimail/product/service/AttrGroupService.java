package com.ctz.gulimail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctz.common.utils.PageUtils;
import com.ctz.gulimail.product.entity.AttrEntity;
import com.ctz.gulimail.product.entity.AttrGroupEntity;
import com.ctz.gulimail.product.vo.AttrGroupRespVo;
import com.ctz.gulimail.product.vo.SkuItemVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-12 15:50:15
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);


    PageUtils queryPageByCatelogId(Map<String, Object> params, Long catelogId);

    List<AttrEntity> getAttrByGroupId(Long attrgroupId);

    List<AttrGroupRespVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);

    List<SkuItemVo.SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId);
}

