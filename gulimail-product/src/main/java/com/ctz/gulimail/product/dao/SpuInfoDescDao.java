package com.ctz.gulimail.product.dao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Collection;

import com.ctz.gulimail.product.entity.SpuInfoDesc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 1
* @description 针对表【pms_spu_info_desc(spu信息介绍)】的数据库操作Mapper
* @createDate 2022-09-29 10:57:25
* @Entity com.ctz.gulimail.product.entity.SpuInfoDesc
*/
@Mapper
public interface SpuInfoDescDao extends BaseMapper<SpuInfoDesc> {

    int insertBatch(@Param("spuInfoDescCollection") Collection<SpuInfoDesc> spuInfoDescCollection);
}




