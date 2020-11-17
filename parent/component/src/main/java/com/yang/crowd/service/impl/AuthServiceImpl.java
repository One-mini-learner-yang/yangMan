package com.yang.crowd.service.impl;

import com.yang.crowd.entity.Auth;
import com.yang.crowd.entity.AuthExample;
import com.yang.crowd.mapper.AuthMapper;
import com.yang.crowd.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthMapper authMapper;
    @Override
    public List<Auth> getAll() {
        return authMapper.selectByExample(new AuthExample());
    }

    @Override
    public void saveAuth(Auth auth) {

    }

    @Override
    public void updateAuth(Auth auth) {

    }

    @Override
    public void deleteAuth(Integer id) {

    }

    @Override
    public List<Integer> getAssignedAuthByRoleId(Integer roleId) {
        return authMapper.getAssignedAuthByRoleId(roleId);
    }

    @Override
    public void saveRoleAuthRelationship(Map<String, List<Integer>> map) {
        List<Integer> roleIdList=map.get("roleId");
        Integer roleId=roleIdList.get(0);
        authMapper.deleteOldRelationship(roleId);
        List<Integer> authIdList=map.get("authIdArray");
        if (authIdList!=null&&authIdList.size()>0){
            authMapper.insertNewRelationship(roleId,authIdList);
        }
    }
}