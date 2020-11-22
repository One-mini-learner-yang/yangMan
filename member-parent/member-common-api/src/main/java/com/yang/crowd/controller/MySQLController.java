package com.yang.crowd.controller;

import com.yang.crowd.service.MySQLRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class MySQLController {
    @Resource
    private MySQLRemoteService mySQLRemoteService;
    @RequestMapping("/test")
    public void test(){
        log.info("1111");
        log.info(String.valueOf(mySQLRemoteService.getMemberPOByLoginAcctRemote("jack")));
    }
}
