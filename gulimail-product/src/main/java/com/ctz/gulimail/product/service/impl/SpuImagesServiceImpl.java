package com.ctz.gulimail.product.service.impl;

import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ctz.gulimail.product.dao.SpuImagesDao;
import com.ctz.gulimail.product.entity.SpuImagesEntity;
import com.ctz.gulimail.product.service.SpuImagesService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Autowired
    private SpuImagesDao spuImagesDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveImagesBatch(Long id, List<String> images) {
        if(!CollectionUtils.isEmpty(images)){
            List<SpuImagesEntity> collect = images.stream().filter(item->!StringUtils.isEmpty(item)).map((s) -> {
                SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
                spuImagesEntity.setSpuId(id);
                spuImagesEntity.setImgUrl(s);
                return spuImagesEntity;
            }).collect(Collectors.toList());

            spuImagesDao.insertBatch(collect);
        }
    }

}