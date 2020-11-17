package com.yang.crowd.service.impl;

import com.yang.crowd.entity.po.MemberPO;
import com.yang.crowd.entity.po.MemberPOExample;
import com.yang.crowd.mapper.MemberPOMapper;
import com.yang.crowd.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return  memberPOMapper.selectByExample(memberPOExample).get(0);
    }
}
