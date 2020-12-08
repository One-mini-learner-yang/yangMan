package com.yang.crowd.service.impl;

import com.yang.crowd.entity.po.MemberConfirmInfoPO;
import com.yang.crowd.entity.po.MemberLaunchInfoPO;
import com.yang.crowd.entity.po.ProjectPO;
import com.yang.crowd.entity.vo.*;
import com.yang.crowd.mapper.*;
import com.yang.crowd.service.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        memberLaunchInfoPO.setMemberid(memberId);
        memberLaunchInfoPOMapper.insertSelective(memberLaunchInfoPO);
//        保存项目汇报信息
        List<ReturnVO> returnPOList=projectVO.getReturnVOList();
        returnPOMapper.insertReturnPOBatch(returnPOList,id);
//        保存项目确认信息
        MemberConfirmInfoVO memberConfirmInfoVO=projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO=new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO,memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberId);
        memberConfirmInfoPOMapper.insertSelective(memberConfirmInfoPO);
    }

    @Override
    public List<ProjectTypeVO> selectPortalTypeVOList() {
        return  projectPOMapper.selectPortalTypeVOList();
    }

    @Override
    public DetailProjectVO selectDetailProjectVO(Integer projectId) {
        DetailProjectVO detailProjectVO= projectPOMapper.selectDetailProjectVO(projectId);
        Integer status=detailProjectVO.getStatus();
        switch (status){
            case 0:
                detailProjectVO.setStatusText("审核中");
                break;
            case 1:
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2:
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3:
                detailProjectVO.setStatusText("已关闭");
                break;
            default:
                break;
        }
        String deployDate=detailProjectVO.getDeployDate();
        Date currentDay=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date deployDay=simpleDateFormat.parse(deployDate);
            long currentTimeStamp=currentDay.getTime();
            long deployTimeStamp=deployDay.getTime();
            long pastDay=(currentTimeStamp-deployTimeStamp)/1000/60/60/24;
            Integer totalDays=detailProjectVO.getDay();
            Integer lastDay=(int)(totalDays-pastDay);
            detailProjectVO.setLastDay(lastDay);
        }catch (Exception e){
            e.printStackTrace();
        }
        return detailProjectVO;
    }


}
