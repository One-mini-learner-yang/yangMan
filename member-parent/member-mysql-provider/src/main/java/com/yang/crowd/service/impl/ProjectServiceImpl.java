package com.yang.crowd.service.impl;

import com.yang.crowd.entity.MemberConfirmInfoPO;
import com.yang.crowd.entity.po.MemberLaunchInfoPO;
import com.yang.crowd.entity.po.ProjectPO;
import com.yang.crowd.entity.vo.MemberConfirmInfoVO;
import com.yang.crowd.entity.vo.MemberLauchInfoVO;
import com.yang.crowd.entity.vo.ProjectVO;
import com.yang.crowd.entity.vo.ReturnVO;
import com.yang.crowd.mapper.*;
import com.yang.crowd.service.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectPOMapper projectPOMapper;
    @Autowired
    private ProjectItemPicPOMapper projectItemPicPOMapper;
    @Autowired
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;
    @Autowired
    private ReturnPOMapper returnPOMapper;
    @Autowired
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;
    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public void saveProject(ProjectVO projectVO, Integer memberId) {
        ProjectPO projectPO=new ProjectPO();
        BeanUtils.copyProperties(projectVO,projectPO);
        projectPOMapper.insertSelective(projectPO);
        Integer id=projectPO.getId();

//        保存项目分类的关联信息
        List<Integer> typeIdList=projectVO.getTypeIdList();
        projectPOMapper.insertTypeRelationShip(typeIdList,id);
//        保存项目标签的关联信息
        List<Integer> tagIdList=projectVO.getTagIdList();
        projectPOMapper.insertTagRelationship(tagIdList,id);
//        保存详情图片的路径信息
        List<String> detailPicturePathList=projectVO.getDetailPicturePathList();
        projectItemPicPOMapper.insertPathList(detailPicturePathList,id);
//        保存项目发起人信息
        MemberLauchInfoVO memberLauchInfoVO=projectVO.getMemberLauchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO=new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLauchInfoVO,memberLaunchInfoPO);
        memberLaunchInfoPOMapper.insertSelective(memberLaunchInfoPO);
//        保存项目汇报信息
        List<ReturnVO> returnPOList=projectVO.getReturnVOList();
        returnPOMapper.insertReturnPOBatch(returnPOList,id);
//        保存项目确认信息
        MemberConfirmInfoVO memberConfirmInfoVO=projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO=new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO,memberConfirmInfoPO);
        memberConfirmInfoPOMapper.insertSelective(memberConfirmInfoPO);
    }
}
