package com.ctz.gulimail.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctz.common.utils.PageUtils;
import com.ctz.gulimail.member.entity.MemberEntity;
import com.ctz.gulimail.member.vo.MemberLoginVO;
import com.ctz.gulimail.member.vo.MemberRegistVo;
import com.ctz.gulimail.member.vo.SocialUser;

import java.util.Map;

/**
 * 会员
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-14 15:30:15
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void regist(MemberRegistVo vo);

    MemberEntity login(MemberLoginVO vo);

    MemberEntity login(SocialUser vo);
}

