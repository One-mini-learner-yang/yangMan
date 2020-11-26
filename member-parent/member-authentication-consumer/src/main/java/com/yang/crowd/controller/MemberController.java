package com.yang.crowd.controller;

import com.yang.crowd.config.ShortMessageProperties;
import com.yang.crowd.constant.CrowdConstant;
import com.yang.crowd.entity.po.MemberPO;
import com.yang.crowd.entity.vo.MemberLoginVO;
import com.yang.crowd.entity.vo.MemberVO;
import com.yang.crowd.service.MySQLRemoteService;
import com.yang.crowd.service.RedisRemoteService;
import com.yang.crowd.util.CrowdUtil;
import com.yang.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;
@Slf4j
@Controller
public class MemberController {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedisRemoteService redisRemoteService;
    @Autowired
    private MySQLRemoteService mySQLRemoteService;
    @Autowired
    private ShortMessageProperties shortMessageProperties;
    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(String phoneNum){
        ResultEntity<String>  sendMessageResultEntity=CrowdUtil.sendCodeByShortMessage(shortMessageProperties.getHost(),
                                         shortMessageProperties.getPath(),
                                         shortMessageProperties.getMethod(),
                                         phoneNum,
                                         shortMessageProperties.getAppCode(),
                                         shortMessageProperties.getSkin());
        log.info(String.valueOf(sendMessageResultEntity));
        if(ResultEntity.SUCCESS.equals(sendMessageResultEntity.getResult())){
            String code=sendMessageResultEntity.getData();
            String key= CrowdConstant.REDIS_CODE_PREFIX+phoneNum;
            ResultEntity<String> saveCodeResultEntity=redisRemoteService.setRedisKeyValueWithTimeOutRemote(key,code, (long) 15, TimeUnit.MINUTES);
            log.info(String.valueOf(saveCodeResultEntity));
            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())){
                log.info("success");
                return ResultEntity.successWithoutData();
            }else {
                return saveCodeResultEntity;
            }
        }else {
            return sendMessageResultEntity;
        }
    }

    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap){
        String phoneNum=memberVO.getPhoneNum();
        String loginacct=memberVO.getLoginacct();
        log.info(loginacct);
        String key=CrowdConstant.REDIS_CODE_PREFIX+phoneNum;
        ResultEntity<String> resultEntity=redisRemoteService.getRedisStringValueByKeyRemote(key);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            if (!mySQLRemoteService.judgeExistMember(loginacct).getData()){
                if (resultEntity.getData()!=null&&(memberVO.getCode().equals(resultEntity.getData()))){
                    redisRemoteService.removeRedisKeyRemote(key);
                    BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
                    String userpswd=passwordEncoder.encode(memberVO.getUserpswd());
                    memberVO.setUserpswd(userpswd);
                    MemberPO memberPO=new MemberPO();
                    BeanUtils.copyProperties(memberVO,memberPO);
                    ResultEntity<String> saveMemberResultEntity=mySQLRemoteService.saveMember(memberPO);
                    if (ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())){
                        modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveMemberResultEntity.getMessage());
                        return "member-reg";
                    }
                    return "redirect:http://localhost/auth/member/to/login/page";
                }else {
                    modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_CODE_IS_WRONG);
                    return "member-reg";
                }
            }else {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
                return "member-reg";
            }
        }else{
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,resultEntity.getMessage());
            return "member-reg";
        }
    }

    @RequestMapping("/auth/member/do/login")
    public String doLogin(String loginacct, String userpswd, ModelMap modelMap, HttpSession session, HttpServletResponse response){
        ResultEntity<MemberPO> resultEntity=mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);
        MemberPO memberPO=resultEntity.getData();
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            if (resultEntity.getData()!=null){
                BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
                String userPswdDataBase=memberPO.getUserpswd();
                if (bCryptPasswordEncoder.matches(userpswd,userPswdDataBase)){
                    MemberLoginVO memberLoginVO=new MemberLoginVO(memberPO.getId(),memberPO.getUsername(),memberPO.getEmail());
                    session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER,memberLoginVO);
                    redisTemplate.opsForValue().set(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, String.valueOf(memberLoginVO));
                    return "redirect:http://localhost/auth/member/to/center/page";
                }else {
                    modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);
                    return "member-login";
                }
            }else {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);
                return "member-login";
            }
        }else {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,resultEntity.getMessage());
            return "member-login";
        }
    }
    @RequestMapping("/auth/member/logout")
    public String logOut(HttpSession session){
        redisTemplate.delete(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        session.invalidate();
        return "redirect:http://localhost/";
    }
}
