package com.yang.crowd.controller;

import com.netflix.discovery.converters.Auto;
import com.yang.crowd.constant.CrowdConstant;
import com.yang.crowd.entity.vo.MemberConfirmInfoVO;
import com.yang.crowd.entity.vo.MemberLoginVO;
import com.yang.crowd.entity.vo.ProjectVO;
import com.yang.crowd.entity.vo.ReturnVO;
import com.yang.crowd.service.MySQLRemoteService;
import com.yang.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Controller
public class ProjectConsumerController {
    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/project/create/project/information")
    public String saveProjectBasicInfo(ProjectVO projectVO, MultipartFile headerPicture, List<MultipartFile> detailPictureList, HttpSession session,ModelMap modelMap) throws IOException {
        log.info(projectVO.toString());
        boolean headPictureIsEmpty=headerPicture.isEmpty();
        if (!headPictureIsEmpty){
            try{
//                此处部署到tomcat后使用
//                String headPicPath=request.getServletContext().getRealPath("/")+headerPicture.getOriginalFilename();
                String headPicPath="D:\\IdeaProjects\\yangMan\\member-parent\\member-project-consumer\\src\\main\\resources\\launch-upload\\"+headerPicture.getOriginalFilename();
                log.info(headPicPath);
                File file=new File(headPicPath);
                FileOutputStream fileOutputStream=new FileOutputStream(file);
                fileOutputStream.write(headerPicture.getBytes());
                projectVO.setHeaderPicturePath(headPicPath);
            }catch (Exception e){
                e.printStackTrace();
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAID);
                return "project-launch";
            }
        }else {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }
        List<String> detailPicPaths=new ArrayList<String>();
        if (detailPictureList==null||detailPictureList.size()==0){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAL_PIC_EMPTY);
            return "project-launch";
        }
        for (MultipartFile detailPicture:detailPictureList){
            if (!detailPicture.isEmpty()){
                try{
//                    此处部署到tomcat后使用
//                    String detailPicPath=request.getServletContext().getRealPath("/")+detailPicture.getOriginalFilename();
                    String detailPicPath="D:\\IdeaProjects\\yangMan\\member-parent\\member-project-consumer\\src\\main\\resources\\launch-upload\\"+detailPicture.getOriginalFilename();
                    File file=new File(detailPicPath);
                    FileOutputStream fileOutputStream=new FileOutputStream(file);
                    fileOutputStream.write(detailPicture.getBytes());
                    detailPicPaths.add(detailPicPath);
                }catch (Exception e){
                    e.printStackTrace();
                    modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAL_PIC_UPLOAD_FAILD);
                    return "project-launch";
                }
            }else {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAL_PIC_EMPTY);
                return "project-launch";
            }
        }
        projectVO.setDetailPicturePathList(detailPicPaths);
        log.info(projectVO.toString());
        session.setAttribute(CrowdConstant.ATTR_MANE_TEMPLE_PROJECT,projectVO);
        return "redirect:http://localhost/project/return/info/page";
    }

    @ResponseBody
    @RequestMapping("/project/create/upload/return/picture.json")
    public ResultEntity<String> uploadReturnPicture(MultipartFile returnPicture) throws FileNotFoundException {
        String returnPicturePath="D:\\IdeaProjects\\yangMan\\member-parent\\member-project-consumer\\src\\main\\resources\\return-upload\\"+returnPicture.getOriginalFilename();
        File file=new File(returnPicturePath);
        FileOutputStream fileOutputStream=new FileOutputStream(file);
        try{
            fileOutputStream.write(returnPicture.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
        return ResultEntity.successWithData(returnPicturePath);
    }
    @ResponseBody
    @RequestMapping("/project/create/save/return.json")
    public ResultEntity<String> saveReturn(ReturnVO returnVO,HttpSession session){
       try{
           ProjectVO projectVO= (ProjectVO) session.getAttribute(CrowdConstant.ATTR_MANE_TEMPLE_PROJECT);
           if (projectVO==null){
               return ResultEntity.failed(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
           }
           List<ReturnVO> returnVOList=projectVO.getReturnVOList();
           if (returnVOList==null||returnVOList.size()==0){
               returnVOList=new ArrayList<>();
               projectVO.setReturnVOList(returnVOList);
           }
           returnVOList.add(returnVO);
           session.setAttribute(CrowdConstant.ATTR_MANE_TEMPLE_PROJECT,projectVO);
           return ResultEntity.successWithoutData();
       } catch (Exception e) {
           e.printStackTrace();
           return ResultEntity.failed(e.getMessage());
       }
    }
    @RequestMapping("/project/create/remove/return/{order}.html")
    public String updateReturn(@PathVariable("order")Integer order,HttpSession session){
        log.info(order.toString());
        ProjectVO projectVO= (ProjectVO) session.getAttribute(CrowdConstant.ATTR_MANE_TEMPLE_PROJECT);
        if (projectVO==null){
            throw new RuntimeException(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
        }
        List<ReturnVO> returnVOList=projectVO.getReturnVOList();
        ReturnVO returnVO=returnVOList.get(order);
        returnVOList.remove(returnVO);
        projectVO.setReturnVOList(returnVOList);
        log.info(projectVO.toString());
        session.setAttribute(CrowdConstant.ATTR_MANE_TEMPLE_PROJECT,projectVO);
        return "redirect:http://localhost/project/return/info/page";
    }
    @RequestMapping("/project/create/confirm")
    public String  saveConfirm(HttpSession session, MemberConfirmInfoVO memberConfirmInfoVO,ModelMap modelMap){
        ProjectVO projectVO= (ProjectVO) session.getAttribute(CrowdConstant.ATTR_MANE_TEMPLE_PROJECT);
        if (projectVO==null){
            throw new RuntimeException(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
        }
        log.info(projectVO.toString());
        MemberLoginVO memberLoginVO= (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId=memberLoginVO.getId();
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);
        ResultEntity<String> resultEntity=mySQLRemoteService.saveProjectVORemote(projectVO,memberId);
        String result=resultEntity.getResult();
        if (ResultEntity.FAILED.equals(result)){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,resultEntity.getMessage());
            return "project-confirm";
        }
        session.removeAttribute(CrowdConstant.ATTR_MANE_TEMPLE_PROJECT);
        return "redirect:http://localhost/project/create/success";
    }
}
