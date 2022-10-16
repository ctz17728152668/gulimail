package com.ctz.gulimail.search.vo;

import lombok.Data;

import java.util.List;

@Data
public class SearchParam {
    /**
     * 全文匹配关键字
     */
    private String keyword;
    /**
     * 三级分类
     */
    private Long catalog3Id;

    /**
     * 自定义排序
     */
    private String sort;

    /**
     * 是否有库存
     */
    private Integer hasStock;
    /**
     * 价格区间
     * 1_500 1_ _500
     */
    private String skuPrice;
    /**
     * 品牌id
     */
    private List<Long> brandId;
    /**
     * 规格属性
     * attrs=1_其他:安卓
     */
    private List<String> attrs;

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * _开头 表明为源数据
     * 请求参数字符串
     */
    private String _queryString;
}
