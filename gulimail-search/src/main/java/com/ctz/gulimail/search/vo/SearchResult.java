package com.ctz.gulimail.search.vo;

import com.ctz.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 * 返回给页面的所有信息
 */
@Data
public class SearchResult {

    /**
     * 查询到的所有商品信息
     */
    private List<SkuEsModel> products;

    /**
     * 分页信息
     */
    private Integer pageNum;
    private Long total;
    private Integer totalPages;
    private List<Integer> pageNavs;

    /**
     * 当前查询到的结果涉及到的所有品牌
     */
    private List<BrandVo> brands;

    /**
     * 当前查询到的结果涉及到的所有属性
     */
    private List<AttrVo> attrs;

    /**
     * 当前查询道德结果，涉及到的所有分类
     */
    private List<CatalogVo> catalogs;

    /**
     * 面包屑导航数据
     */
    private List<NavVo> navs;


    @Data
    public static class NavVo {
        private String navName;
        private String navValue;
        private String link;
    }

    @Data
    public static class BrandVo {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    public static class CatalogVo {
        private Long catalogId;
        private String catalogName;
    }

    @Data
    public static class AttrVo {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
}
