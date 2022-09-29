package com.ctz.gulimail.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ctz.common.constant.ProductConstant;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;
import com.ctz.gulimail.product.entity.AttrAttrgroupRelationEntity;
import com.ctz.gulimail.product.entity.AttrGroupEntity;
import com.ctz.gulimail.product.entity.CategoryEntity;
import com.ctz.gulimail.product.service.AttrAttrgroupRelationService;
import com.ctz.gulimail.product.service.AttrGroupService;
import com.ctz.gulimail.product.service.CategoryService;
import com.ctz.gulimail.product.vo.AttrRespVo;
import com.ctz.gulimail.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ctz.gulimail.product.dao.AttrDao;
import com.ctz.gulimail.product.entity.AttrEntity;
import com.ctz.gulimail.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrGroupService attrGroupService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        save(attrEntity);
        //仅当为基本属性时有分组
        if(attr.getAttrType()== ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationService.save(attrAttrgroupRelationEntity);
        }
    }

    @Override
    public PageUtils attrList(Map<String, Object> params, Long catelogId, String type) {
        LambdaQueryWrapper<AttrEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AttrEntity::getAttrType,"base".equalsIgnoreCase(type)?ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode():ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        if(catelogId!=0){
            wrapper.eq(AttrEntity::getCatelogId,catelogId);
        }

        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((queryWrapper)->{
                queryWrapper.eq(AttrEntity::getAttrId,key).or().like(AttrEntity::getAttrName,key);
            });
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        List<AttrRespVo> collect = page.getRecords().stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

            //设置分类名字
            attrRespVo.setCatelogName(categoryService.getById(attrEntity.getCatelogId()).getName());

            //只有基本属性才有分组
            if("base".equalsIgnoreCase(type)){
                //设置分组名字
                AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationService.getOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId()));
                if (attrAttrgroupRelationEntity != null) {
                    AttrGroupEntity attrGroupEntity = attrGroupService.getById(attrAttrgroupRelationEntity.getAttrGroupId());
                    if(attrGroupEntity!=null){
                        attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                    }
                }
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(collect);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrById(Long attrId) {
        AttrEntity attr = getById(attrId);
        AttrRespVo attrRespVo = new AttrRespVo();
        BeanUtils.copyProperties(attr,attrRespVo);

        //仅当基本属性有分组
        if(ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()==attr.getAttrType()){
            AttrAttrgroupRelationEntity relation = attrAttrgroupRelationService.getOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().
                    eq(AttrAttrgroupRelationEntity::getAttrId, attrId));
            if(relation!=null){
                AttrGroupEntity group = attrGroupService.getById(relation.getAttrGroupId());
                attrRespVo.setAttrGroupId(group.getAttrGroupId());
                attrRespVo.setGroupName(group.getAttrGroupName());
            }
        }

        Long catelogId = attr.getCatelogId();

        CategoryEntity category = categoryService.getById(catelogId);
        if(category!=null){
            attrRespVo.setCatelogName(category.getName());
        }

        Long[] path = categoryService.getPath(catelogId);
        attrRespVo.setCatelogPath(path);
        return attrRespVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        updateById(attrEntity);

        //基本属性才需要修改关联
        if(attr.getAttrType()==ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity relation = new AttrAttrgroupRelationEntity();
            relation.setAttrGroupId(attr.getAttrGroupId());
            relation.setAttrId(attr.getAttrId());
            int count = attrAttrgroupRelationService.count(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq(AttrAttrgroupRelationEntity::getAttrId, attr.getAttrId()));
            //若之前存在 则为更新操作
            if (count > 0) {
                attrAttrgroupRelationService.update(relation, new LambdaUpdateWrapper<AttrAttrgroupRelationEntity>()
                        .set(AttrAttrgroupRelationEntity::getAttrGroupId, relation.getAttrGroupId())
                        .eq(AttrAttrgroupRelationEntity::getAttrId, attr.getAttrId())
                );
            } else {
                //否则为插入操作
                attrAttrgroupRelationService.save(relation);
            }
        }


    }

    /**
     * 找出本分类下的 非当前分组 且没有被其他分组关联的属性
     * @param params
     * @param attrgroupId
     * @return
     */
    @Override
    public PageUtils getNoattrByGroupId(Map<String, Object> params, Long attrgroupId) {
        AttrGroupEntity group = attrGroupService.getById(attrgroupId);
        Long catelogId = group.getCatelogId();
        //1.找出所属分类下的其他分组
        List<Long> groupIds = attrGroupService.list(new LambdaQueryWrapper<AttrGroupEntity>()
                .eq(AttrGroupEntity::getCatelogId, catelogId))
//                .ne(AttrGroupEntity::getAttrGroupId, attrgroupId))
                .stream().map(AttrGroupEntity::getAttrGroupId)
                .collect(Collectors.toList());



        //2.找出这些分组下的所有属性id
        List<Long> attrIds = attrAttrgroupRelationService.list(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                .in(!CollectionUtils.isEmpty(groupIds),AttrAttrgroupRelationEntity::getAttrGroupId, groupIds))
                .stream().map(AttrAttrgroupRelationEntity::getAttrId)
                .collect(Collectors.toList());

        //3.找出所有属性 除去不符合的属性
        LambdaQueryWrapper<AttrEntity> wrapper = new LambdaQueryWrapper<AttrEntity>()
                .eq(AttrEntity::getCatelogId, catelogId)
                .notIn(!CollectionUtils.isEmpty(attrIds),AttrEntity::getAttrId, attrIds)
                .eq(AttrEntity::getAttrType,ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());


        //4.设置分页 模糊查询
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and(w->{
               w.eq(AttrEntity::getAttrId,key).or().like(AttrEntity::getAttrName,key);
            });
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}