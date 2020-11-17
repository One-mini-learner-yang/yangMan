package com.yang.crowd.service;

import com.yang.crowd.entity.po.MemberPO;
import com.yang.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("YANG-CROWD-MYSQL")
public interface MySQLRemoteService {
    @RequestMapping("/get/memberPO/by/login/acct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(String loginAcct);
    @RequestMapping("/port")
    void port();
}
