package com.yang.crowd.service.impl;

import com.yang.crowd.entity.po.AddressPO;
import com.yang.crowd.entity.po.AddressPOExample;
import com.yang.crowd.entity.vo.AddressVO;
import com.yang.crowd.entity.vo.OrderProjectVO;
import com.yang.crowd.mapper.AddressPOMapper;
import com.yang.crowd.mapper.OrderPOMapper;
import com.yang.crowd.mapper.OrderProjectPOMapper;
import com.yang.crowd.service.OrderService;
import com.yang.crowd.util.ResultEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderProjectPOMapper orderProjectPOMapper;
    @Autowired
    private OrderPOMapper orderPOMapper;
    @Autowired
    private AddressPOMapper addressPOMapper;

    @Override
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {
        return orderProjectPOMapper.selectOrderProjectVO(returnId);
    }

    @Override
    public OrderProjectVO getOrderProjectVOStaticMoney(Integer projectId, Integer supportMoney) {
        return orderProjectPOMapper.selectOrderProjectVOStaticMoney(projectId);
    }

    @Override
    public List<AddressVO> getAddressVORemote(Integer memberId) {
        AddressPOExample addressPOExample=new AddressPOExample();
        AddressPOExample.Criteria criteria=addressPOExample.createCriteria();
        criteria.andMemberIdEqualTo(memberId);
        List<AddressPO> list=addressPOMapper.selectByExample(addressPOExample);
        List<AddressVO> addressVOList=new ArrayList<>();
        for(AddressPO addressPO:list){
            AddressVO addressVO=new AddressVO();
            BeanUtils.copyProperties(addressPO,addressVO);
            addressVOList.add(addressVO);
        }
        return addressVOList;
    }

    @Override
    public void saveAddressRemote(AddressVO addressVO) {
        AddressPO addressPO=new AddressPO();
        BeanUtils.copyProperties(addressVO,addressPO);
        addressPOMapper.insertSelective(addressPO);
    }
}
