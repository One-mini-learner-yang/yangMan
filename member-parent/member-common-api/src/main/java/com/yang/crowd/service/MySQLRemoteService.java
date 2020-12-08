package com.yang.crowd.service;

import com.yang.crowd.entity.po.MemberPO;
import com.yang.crowd.entity.vo.*;
import com.yang.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("YANG-CROWD-MYSQL")
public interface MySQLRemoteService {
    @RequestMapping("/get/memberPO/by/login/acct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginAcct") String loginAcct);
    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO);
    @RequestMapping("/judge/exist/member")
    public ResultEntity<Boolean> judgeExistMember(@RequestParam("loginacct")String loginacct);
    @RequestMapping("/saveProjectVORemote")
    public ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO,@RequestParam("memberId") Integer memberId);
    @RequestMapping("/get/protal/type/project/data/remote")
    public ResultEntity<List<ProjectTypeVO>> getPortalTypeProjectDataRemote();
    @RequestMapping("/get/project/detail/remote/{projectid}")
    public ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectid")Integer projectId);
    @RequestMapping("/get/order/project/vo/remote/{projectId}/{returnId}")
    public ResultEntity<OrderProjectVO> getOrderProjectVORemote(@PathVariable("projectId") Integer projectId,@PathVariable("returnId") Integer returnId);
    @RequestMapping("/get/order/project/vo/static/remote/{projectId}/{supportMoney}")
    public ResultEntity<OrderProjectVO> getOrderProjectVOStaticMoneyRemote(@PathVariable("projectId") Integer projectId,@PathVariable("supportMoney") Integer supportMoney);
    @RequestMapping("/get/address/remote")
    public ResultEntity<List<AddressVO>> getAddressVORemote(@RequestParam("memberId") Integer memberId);
    @RequestMapping("/save/address/remote")
    public ResultEntity<String> saveAddressRemote(@RequestBody AddressVO addressVO);
}