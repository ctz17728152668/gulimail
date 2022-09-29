package com.ctz.gulimail.product.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ctz.gulimail.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

@Data
public class AttrGroupRespVo {

    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     * 该分组下的关联的所有属性
     */
    private List<AttrEntity> attrs;

}
