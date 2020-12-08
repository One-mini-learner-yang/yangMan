package com.yang.crowd.service;

import com.yang.crowd.entity.vo.AddressVO;
import com.yang.crowd.entity.vo.OrderProjectVO;
import com.yang.crowd.util.ResultEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface OrderService {
    public OrderProjectVO getOrderProjectVO(Integer projectId,Integer returnId);
    public OrderProjectVO getOrderProjectVOStaticMoney(Integer projectId,Integer supportMoney);
    public List<AddressVO> getAddressVORemote(Integer memberId);
    public void saveAddressRemote(AddressVO addressVO);
}
