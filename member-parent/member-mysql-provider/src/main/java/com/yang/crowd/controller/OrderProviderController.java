package com.yang.crowd.controller;

import com.yang.crowd.entity.vo.AddressVO;
import com.yang.crowd.entity.vo.OrderProjectVO;
import com.yang.crowd.service.OrderService;
import com.yang.crowd.util.ResultEntity;
import org.apache.tomcat.jni.Address;
import org.bouncycastle.est.CACertsResponse;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class OrderProviderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("/get/order/project/vo/remote/{projectId}/{returnId}")
    public ResultEntity<OrderProjectVO> getOrderProjectVORemote(@PathVariable("projectId") Integer projectId, @PathVariable("returnId") Integer returnId){
        try{
            OrderProjectVO orderProjectVO=orderService.getOrderProjectVO(projectId,returnId);
            return ResultEntity.successWithData(orderProjectVO);
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
    @RequestMapping("/get/order/project/vo/static/remote/{projectId}/{supportMoney}")
    public ResultEntity<OrderProjectVO> getOrderProjectVOStaticMoneyRemote(@PathVariable("projectId") Integer projectId,@PathVariable("supportMoney") Integer supportMoney){
        try{
            OrderProjectVO orderProjectVO=orderService.getOrderProjectVOStaticMoney(projectId,supportMoney);
            return ResultEntity.successWithData(orderProjectVO);
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
    @RequestMapping("/get/address/remote")
    public ResultEntity<List<AddressVO>> getAddressVORemote(@RequestParam("memberId") Integer memberId){
        try {
            List<AddressVO> list=orderService.getAddressVORemote(memberId);
            return ResultEntity.successWithData(list);
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
    @RequestMapping("/save/address/remote")
    public ResultEntity<String> saveAddressRemote(@RequestBody AddressVO addressVO){
        try {
            orderService.saveAddressRemote(addressVO);
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
}
