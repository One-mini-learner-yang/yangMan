package com.yang.crowd.controller;

import com.yang.crowd.entity.vo.DetailProjectVO;
import com.yang.crowd.entity.vo.ProjectTypeVO;
import com.yang.crowd.entity.vo.ProjectVO;
import com.yang.crowd.service.ProjectService;
import com.yang.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
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

    @RequestMapping("/get/protal/type/project/data/remote")
    public ResultEntity<List<ProjectTypeVO>> getPortalTypeProjectDataRemote(){
        try {
            List<ProjectTypeVO> projectTypeVOList=projectService.selectPortalTypeVOList();
            log.info(projectTypeVOList.toString());
            return ResultEntity.successWithData(projectTypeVOList);
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
    @RequestMapping("/get/project/detail/remote/{projectid}")
    public ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectid")Integer projectId){
        try{
            DetailProjectVO detailProjectVO=projectService.selectDetailProjectVO(projectId);
            return ResultEntity.successWithData(detailProjectVO);
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
}
