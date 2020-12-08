package com.yang.crowd.service;

import com.yang.crowd.entity.vo.DetailProjectVO;
import com.yang.crowd.entity.vo.ProjectTypeVO;
import com.yang.crowd.entity.vo.ProjectVO;

import java.util.List;

public interface ProjectService {
    public void saveProject(ProjectVO projectVO,Integer memberId);

    public List<ProjectTypeVO> selectPortalTypeVOList();

    public DetailProjectVO selectDetailProjectVO(Integer projectId);
}
