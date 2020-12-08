package com.yang.crowd.controller;

import com.yang.crowd.constant.CrowdConstant;
import com.yang.crowd.entity.vo.AddressVO;
import com.yang.crowd.entity.vo.MemberLoginVO;
import com.yang.crowd.entity.vo.OrderProjectVO;
import com.yang.crowd.service.MySQLRemoteService;
import com.yang.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
public class OrderController {
    @Autowired
    private MySQLRemoteService mySQLRemoteService;
    @RequestMapping("/order/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirm(@PathVariable("projectId")Integer projectId, @PathVariable("returnId")Integer returnId, HttpSession session){
        ResultEntity<OrderProjectVO> resultEntity=mySQLRemoteService.getOrderProjectVORemote(projectId,returnId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            session.setAttribute("orderProjectVO",resultEntity.getData());
        }
        return "confirm-return";
    }
    @RequestMapping("/order/confirm/static/return/info/{projectId}/{supportMoney}")
    public String showReturnStaticMoneyConfirm(@PathVariable("projectId")Integer projectId,@PathVariable("supportMoney")Integer supportMoney,HttpSession session){
        log.info(supportMoney.toString());
        ResultEntity<OrderProjectVO> resultEntity=mySQLRemoteService.getOrderProjectVOStaticMoneyRemote(projectId,supportMoney);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            OrderProjectVO orderProjectVO=resultEntity.getData();
            orderProjectVO.setSupportPrice(supportMoney);
            orderProjectVO.setReturnContent("每满1750人抽取一份豪华至尊早餐，至少抽取一份。抽取名额（小数点后一位四舍五入）=参与人数÷1750人，由杨式兄弟早餐铺抽取。");
            orderProjectVO.setReturnCount(0);
            orderProjectVO.setFreight(0);
            orderProjectVO.setSignalPurchase(0);
            orderProjectVO.setPurchase(0);
            session.setAttribute("orderProjectVO",orderProjectVO);
        }
        return "confirm-return";
    }

    @RequestMapping("/order/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(@PathVariable("returnCount")Integer returnCount,HttpSession session){
        OrderProjectVO orderProjectVO= (OrderProjectVO) session.getAttribute("orderProjectVO");
        orderProjectVO.setReturnCount(returnCount);
        session.setAttribute("orderProjectVO",orderProjectVO);
        MemberLoginVO memberLoginVO= (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer id=memberLoginVO.getId();
        ResultEntity<List<AddressVO>> resultEntity=mySQLRemoteService.getAddressVORemote(id);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            List<AddressVO> list=resultEntity.getData();
            session.setAttribute("addressVOList",list);
        }
        return "confirm-order";
    }
    @RequestMapping("/order/save/address")
    public String saveAddress(AddressVO addressVO,HttpSession session){
        ResultEntity<String> resultEntity=mySQLRemoteService.saveAddressRemote(addressVO);
        log.debug("地址保存处理结果"+resultEntity.getResult());
        OrderProjectVO orderProjectVO= (OrderProjectVO) session.getAttribute("orderProjectVO");
        Integer returnCount=orderProjectVO.getReturnCount();
        return "redirect:http://localhost/order/confirm/order/"+returnCount;
    }


}
