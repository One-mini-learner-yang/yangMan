package com.yang.crowd.controller;

import com.yang.crowd.entity.vo.ProjectVO;
import com.yang.crowd.service.ProjectService;
import com.yang.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectProviderController {

    @Autowired
    private ProjectService projectService;
    @RequestMapping("/saveProjectVORemote")
    public ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam("memberId") Integer memberId){
        try{
            projectService.saveProject(projectVO,memberId);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
}
