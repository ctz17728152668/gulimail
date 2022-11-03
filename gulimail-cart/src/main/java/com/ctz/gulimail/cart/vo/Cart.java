package com.ctz.gulimail.cart.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    /**
     * 购物车内所有商品
     */
    private List<CartItem> items;
    /**
     * 购物车内商品总数
     */
    private Integer countNum;
    /**
     * 购物车内商品种类总数
     */
    private Integer countType;
    /**
     * 商品总价
     */
    private BigDecimal totalAmount;
    /**
     * 商品总共减免价格
     */
    private BigDecimal reduce = new BigDecimal("0.00");

    public List<CartItem> getItems() {
        return items;
    }

    public Integer getCountNum() {
        return items.stream().map(CartItem::getCount)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public Integer getCountType() {
        return items.size();
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = new BigDecimal("0");
        // 计算购物项总价
        if (!CollectionUtils.isEmpty(items)) {
            for (CartItem cartItem : items) {
                if (cartItem.getCheck()) {
                    total = total.add(cartItem.getTotalPrice());
                }
            }
        }
        total = total.subtract(getReduce());
        return total;
    }

    public BigDecimal getReduce() {
        return reduce;
    }
}
