package com.ctz.gulimail.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctz.gulimail.product.entity.SpuInfoDesc;
import com.ctz.gulimail.product.service.SpuInfoDescService;
import com.ctz.gulimail.product.dao.SpuInfoDescDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 1
* @description 针对表【pms_spu_info_desc(spu信息介绍)】的数据库操作Service实现
* @createDate 2022-09-29 10:57:25
*/
@Service
public class SpuInfoDescServiceImpl extends ServiceImpl<SpuInfoDescDao, SpuInfoDesc>
    implements SpuInfoDescService{

    @Autowired
    private SpuInfoDescDao spuInfoDescDao;

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveDescBatch(Long id, List<String> decript) {
        SpuInfoDesc spuInfoDesc = new SpuInfoDesc();
        spuInfoDesc.setSpuId(id);
        spuInfoDesc.setDecript(String.join(",",decript));
        spuInfoDescService.save(spuInfoDesc);
    }
}




