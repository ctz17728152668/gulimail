package com.ctz.gulimail.product.service;

import com.ctz.gulimail.product.entity.SpuInfoDesc;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 1
* @description 针对表【pms_spu_info_desc(spu信息介绍)】的数据库操作Service
* @createDate 2022-09-29 10:57:25
*/
public interface SpuInfoDescService extends IService<SpuInfoDesc> {

    void saveDescBatch(Long id, List<String> decript);
}
