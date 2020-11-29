package com.yang.crowd.service;

import com.yang.crowd.entity.vo.ProjectVO;
import com.yang.crowd.util.ResultEntity;

public interface ProjectService {
    public void saveProject(ProjectVO projectVO,Integer memberId);
}
