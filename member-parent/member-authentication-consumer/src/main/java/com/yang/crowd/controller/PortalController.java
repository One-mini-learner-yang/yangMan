package com.yang.crowd.controller;

import com.yang.crowd.constant.CrowdConstant;
import com.yang.crowd.entity.vo.ProjectTypeVO;
import com.yang.crowd.service.MySQLRemoteService;
import com.yang.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Slf4j
@Controller
public class PortalController {
    @Autowired
    private MySQLRemoteService mySQLRemoteService;
    @RequestMapping("/")
    public String showPortalPage(ModelMap modelMap){

        //实际开发需要加载主页所需数据
        ResultEntity<List<ProjectTypeVO>> resultEntity=mySQLRemoteService.getPortalTypeProjectDataRemote();
        String result=resultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(result)){
            log.info(resultEntity.getData().toString());
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA,resultEntity.getData());
        }
        return "portal";
    }
}
