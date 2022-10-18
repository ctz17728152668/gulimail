package com.ctz.gulimail.product.vo;

import com.ctz.gulimail.product.entity.SkuImages;
import com.ctz.gulimail.product.entity.SkuInfoEntity;
import com.ctz.gulimail.product.entity.SpuInfoDesc;
import lombok.Data;

import java.util.List;

@Data
public class SkuItemVo {

    /**
     * 1.sku基本信息
     */
    private SkuInfoEntity info;

    /**
     * sku的图片信息
     */
    private List<SkuImages> images;

    /**
     * 3.获取spu的销售属性组合
     */
    private List<SkuItemSaleAttrVo> saleAttr;

    /**
     * 4.获取spu的介绍
     */
    private SpuInfoDesc desc;

    /**
     * 5.获取spu的规格参数信息
     */
    private List<SpuItemAttrGroupVo> groupAttrs;

    /**
     * 6.是否有货
     */
    private Boolean hasStock = Boolean.TRUE;


    @Data
    public static class SkuItemSaleAttrVo {
        private Long attrId;
        private String attrName;
        private List<AttrValueWithSkuIdVo> attrValues;
    }

    @Data
    public static class SpuItemAttrGroupVo {
        private String groupName;
        private List<SpuBaseAttrVo> attrs;
    }

    @Data
    public static class SpuBaseAttrVo {
        private String attrName;
        private String attrValue;
    }

    @Data
    public static class AttrValueWithSkuIdVo {
        private String attrValue;
        private String skuIds;
    }
}
