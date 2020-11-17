package com.yang.crowd.service;

import com.yang.crowd.entity.Auth;

import java.util.List;
import java.util.Map;

public interface AuthService {
    List<Auth> getAll();
    void saveAuth(Auth auth);
    void updateAuth(Auth auth);
    void deleteAuth(Integer id);
    List<Integer> getAssignedAuthByRoleId(Integer roleId);
    void saveRoleAuthRelationship(Map<String,List<Integer>> map);
}
