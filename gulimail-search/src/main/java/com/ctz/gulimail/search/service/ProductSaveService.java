package com.ctz.gulimail.search.service;

import com.ctz.common.to.es.SkuEsModel;
import com.ctz.common.utils.R;

import java.io.IOException;
import java.util.List;

public interface ProductSaveService {

    public boolean productStatusUp(List<SkuEsModel> skuEsModelList) throws IOException;
}
