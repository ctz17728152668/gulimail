package com.ctz.gulimail.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctz.gulimail.member.exception.PhoneExistException;
import com.ctz.gulimail.member.exception.UsernameExistException;
import com.ctz.gulimail.member.service.MemberLevelService;
import com.ctz.gulimail.member.utils.HttpUtils;
import com.ctz.gulimail.member.vo.MemberLoginVO;
import com.ctz.gulimail.member.vo.MemberRegistVo;
import com.ctz.gulimail.member.vo.SocialUser;
import com.ctz.gulimail.member.vo.SocialUserDetail;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;

import com.ctz.gulimail.member.dao.MemberDao;
import com.ctz.gulimail.member.entity.MemberEntity;
import com.ctz.gulimail.member.service.MemberService;
import org.springframework.transaction.annotation.Transactional;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    private MemberLevelService memberLevelService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 内部方法会抛出异常 方法签名处需要声明异常抛出 便于controller处理异常
     * @param vo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void regist(MemberRegistVo vo) throws PhoneExistException,UsernameExistException {
        MemberEntity memberEntity = new MemberEntity();

        //设置默认会员等级id
        Long levelId = memberLevelService.getDefaultLevel();
        memberEntity.setLevelId(levelId);

        //用户名手机号判断是否重复
        checkPhoneUnique(vo.getUserName());
        checkUsernameUnique(vo.getPhone());


        memberEntity.setUsername(vo.getUserName());
        memberEntity.setMobile(vo.getPhone());

        //密码需要加密不能明文存储
        //盐值在密码中
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(vo.getPassword());
        memberEntity.setPassword(encode);

        save(memberEntity);
    }

    @Override
    public MemberEntity login(MemberLoginVO vo) {
        String loginacct = vo.getLoginacct();
        String password = vo.getPassword();
        MemberEntity memberEntity = getOne(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getMobile, loginacct).or().eq(MemberEntity::getUsername, loginacct));
        if(memberEntity==null){
            return null;
        }
        String passwordDb = memberEntity.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(password, passwordDb);
        return matches?memberEntity:null;
    }

    @Override
    public MemberEntity login(SocialUser vo) {
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",vo.getAccess_token());
        MemberEntity memberEntity = null;
        try {
            HttpResponse get = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "get", Collections.EMPTY_MAP, map);
            String json = EntityUtils.toString(get.getEntity());
            SocialUserDetail socialUserDetail = JSON.parseObject(json, SocialUserDetail.class);
            Integer socialId = socialUserDetail.getId();
            //如果数据库已存在 则更新数据仅登录
            memberEntity = getOne(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getSocialUid, socialId));
            if(memberEntity!=null){
                MemberEntity update = new MemberEntity();
                update.setId(memberEntity.getId());
                update.setAccessToken(vo.getAccess_token());
                update.setExpiresIn(vo.getExpires_in());
                updateById(update);

                memberEntity.setAccessToken(vo.getAccess_token());
                memberEntity.setExpiresIn(vo.getExpires_in());
            } else {
                //数据库不存在 需要注册新用户
                memberEntity = new MemberEntity();
                memberEntity.setNickname(socialUserDetail.getName());

                //设置默认会员等级id
                Long levelId = memberLevelService.getDefaultLevel();
                memberEntity.setLevelId(levelId);

                memberEntity.setCreateTime(LocalDateTime.now());

                memberEntity.setSocialUid(socialId);
                memberEntity.setAccessToken(vo.getAccess_token());
                memberEntity.setExpiresIn(vo.getExpires_in());
                save(memberEntity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return memberEntity;
    }


    private void checkUsernameUnique(String phone) throws PhoneExistException{
        int count = count(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getMobile, phone));
        if(count>0){
            throw new PhoneExistException();
        }
    }

    private void checkPhoneUnique(String userName) throws UsernameExistException{
        int count = count(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getUsername, userName));
        if(count>0){
            throw new UsernameExistException();
        }
    }

}