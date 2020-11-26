package com.yang.crowd.controller;

import com.yang.crowd.constant.CrowdConstant;
import com.yang.crowd.entity.po.MemberPO;
import com.yang.crowd.service.MemberService;
import com.yang.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MemberProviderController {
    @Autowired
    private MemberService memberService;
    @RequestMapping("/get/memberPO/by/login/acct/remote")
    public ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(String loginAcct){
        try{
            MemberPO memberPO=memberService.getMemberPOByLoginAcct(loginAcct);
            return ResultEntity.successWithData(memberPO);
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }


    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO){
        try{
            memberService.saveMember(memberPO);
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            if (e instanceof DuplicateKeyException){
                return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/judge/exist/member")
    public ResultEntity<Boolean> judgeExistMember(@RequestParam("loginacct") String loginacct){
        log.info(loginacct);
        return ResultEntity.successWithData(memberService.isExistMember(loginacct));
    }
}
