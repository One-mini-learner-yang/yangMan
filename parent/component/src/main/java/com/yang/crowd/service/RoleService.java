package com.yang.crowd.service;

import com.github.pagehelper.PageInfo;
import com.yang.crowd.entity.Role;

import java.util.List;

public interface RoleService {
    PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyWord);
    void saveRole(Role role);
    void updateRole(Role role);
    void removeRole(List<Integer> roles);
    List<Role> getAssignedRole(Integer adminId);
    List<Role> getUnAssignedRole(Integer adminId);
}