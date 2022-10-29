package com.ctz.gulimail.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.ctz.common.exception.BizCodeEnume;
import com.ctz.gulimail.member.exception.PhoneExistException;
import com.ctz.gulimail.member.exception.UsernameExistException;
import com.ctz.gulimail.member.vo.MemberLoginVO;
import com.ctz.gulimail.member.vo.MemberRegistVo;
import com.ctz.gulimail.member.vo.SocialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ctz.gulimail.member.entity.MemberEntity;
import com.ctz.gulimail.member.service.MemberService;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.R;



/**
 * 会员
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-14 15:30:15
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;


    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 用户注册
     * @param vo
     * @return
     */
    @PostMapping("regist")
    public R regist(@RequestBody MemberRegistVo vo){
        try {
            memberService.regist(vo);
        } catch (PhoneExistException e) {
            return R.error(BizCodeEnume.PHONE_EXIST_EXCEPTION.getCode(),BizCodeEnume.PHONE_EXIST_EXCEPTION.getMsg());
        } catch (UsernameExistException e){
            return R.error(BizCodeEnume.USERNAME_EXIST_EXCEPTION.getCode(),BizCodeEnume.USERNAME_EXIST_EXCEPTION.getMsg());
        }
        return R.ok();
    }

    @PostMapping("oauth2/login")
    public R oauth2login(@RequestBody SocialUser vo){
        MemberEntity memberEntity = memberService.login(vo);
        if(memberEntity==null){
            return R.error(BizCodeEnume.USERNAME_PASSWORD_INVALID_EXCEPTION.getCode(),BizCodeEnume.USERNAME_PASSWORD_INVALID_EXCEPTION.getMsg());
        }
        // 登录成功后 返回实体
        return R.ok().put("data",memberEntity);
    }

    @PostMapping("login")
    public R login(@RequestBody MemberLoginVO vo){
        MemberEntity memberEntity = memberService.login(vo);
        if(memberEntity==null){
            return R.error(BizCodeEnume.USERNAME_PASSWORD_INVALID_EXCEPTION.getCode(),BizCodeEnume.USERNAME_PASSWORD_INVALID_EXCEPTION.getMsg());
        }
        // TODO 登录成功后的处理
        return R.ok().put("data",memberEntity);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
