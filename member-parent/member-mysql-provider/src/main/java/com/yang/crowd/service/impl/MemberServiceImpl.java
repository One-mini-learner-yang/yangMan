package com.yang.crowd.service.impl;

import com.yang.crowd.entity.po.MemberPO;
import com.yang.crowd.entity.po.MemberPOExample;
import com.yang.crowd.mapper.MemberPOMapper;
import com.yang.crowd.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl  implements MemberService {
    @Autowired
    private MemberPOMapper memberPOMapper;

    @Override
    public MemberPO getMemberPOByLoginAcct(String loginAcct) {
        MemberPOExample memberPOExample=new MemberPOExample();
        MemberPOExample.Criteria criteria=memberPOExample.createCriteria();
        criteria.andLoginacctEqualTo(loginAcct);
        List<MemberPO> list= memberPOMapper.selectByExample(memberPOExample);
        if (list==null||list.size()==0){
            return null;
        }
        return  list.get(0);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public void saveMember(MemberPO memberPO) {
        memberPOMapper.insertSelective(memberPO);
    }

    @Override
    public boolean isExistMember(String loginacct) {
        log.info(loginacct);
        MemberPOExample memberPOExample=new MemberPOExample();
        MemberPOExample.Criteria criteria=memberPOExample.createCriteria();
        criteria.andLoginacctEqualTo(loginacct);
        List<MemberPO> list=memberPOMapper.selectByExample(memberPOExample);
        if (list.isEmpty()){
            log.info("1111");
            return false;
        }else {
            return true;
        }
    }
}
