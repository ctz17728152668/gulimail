package com.ctz.gulimail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctz.common.utils.PageUtils;
import com.ctz.gulimail.product.entity.AttrEntity;
import com.ctz.gulimail.product.vo.AttrRespVo;
import com.ctz.gulimail.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-12 15:50:15
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils attrList(Map<String, Object> params, Long catelogId, String type);

    AttrRespVo getAttrById(Long attrId);

    void updateAttr(AttrVo attr);

    PageUtils getNoattrByGroupId(Map<String, Object> params, Long attrgroupId);

    List<Long> getSearchAttrByIds(List<Long> attrIds);
}

