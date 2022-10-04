package com.ctz.gulimail.search.constants;

public enum EsConstants {

    PRODUCT_INDEX("product");

    private String index;

    EsConstants(String index) {
        this.index = index;
    }

    public String getIndex() {
        return index;
    }
}
