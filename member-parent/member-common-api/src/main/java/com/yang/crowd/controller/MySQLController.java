package com.yang.crowd.controller;

import com.yang.crowd.service.MySQLRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class MySQLController {
    @Resource
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/port")
    public void port(){
        mySQLRemoteService.port();
        log.info("11111");
    }
}
