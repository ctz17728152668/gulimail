package com.ctz.gulimail.ware.vo;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseDoneVo {
    private Long id;
    private List<ItemVo> items;
}
