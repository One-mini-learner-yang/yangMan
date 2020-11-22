package com.yang.crowd.service;

import com.yang.crowd.entity.po.MemberPO;

public interface MemberService {

    public MemberPO getMemberPOByLoginAcct(String loginAcct);
    public void saveMember(MemberPO memberPO);
    public boolean isExistMember(String loginacct);
}

