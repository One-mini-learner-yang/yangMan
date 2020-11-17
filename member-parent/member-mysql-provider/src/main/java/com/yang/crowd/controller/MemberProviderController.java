package com.yang.crowd.controller;

import com.yang.crowd.entity.po.MemberPO;
import com.yang.crowd.service.MemberService;
import com.yang.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MemberProviderController {
    @Autowired
    private MemberService memberService;
    @Value("${server.port}")
    private String port;
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
    @RequestMapping("/port")
    public void port(){
        log.info(port);
    }
}
