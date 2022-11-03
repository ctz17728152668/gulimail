package com.ctz.gulimail.cart.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 商品是否被选中
     * 商品添加到购物车默认为选中状态
     */
    private Boolean check = true;

    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品默认显示图片
     */
    private String image;
    /**
     * 商品选择的套餐
     */
    private List<String> skuAttrValues;
    /**
     * 商品单价
     */
    private BigDecimal price;
    /**
     * 商品选择数量
     */
    private Integer count;

    /**
     * 该商品总价
     */
    private BigDecimal totalPrice;

    public BigDecimal getTotalPrice() {
        return this.price.multiply(new BigDecimal(""+this.count));
    }
}
