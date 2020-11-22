package com.yang.crowd.service;

import com.yang.crowd.entity.po.MemberPO;
import com.yang.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("YANG-CROWD-MYSQL")
public interface MySQLRemoteService {
    @RequestMapping("/get/memberPO/by/login/acct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginAcct") String loginAcct);
    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO);
    @RequestMapping("/judge/exist/member")
    public ResultEntity<Boolean> judgeExistMember(@RequestParam("loginacct")String loginacct);
}