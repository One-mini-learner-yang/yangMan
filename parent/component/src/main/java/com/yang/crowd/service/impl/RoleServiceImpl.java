package com.yang.crowd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yang.crowd.entity.Role;
import com.yang.crowd.entity.RoleExample;
import com.yang.crowd.mapper.RoleMapper;
import com.yang.crowd.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;


    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyWord) {
        PageHelper.startPage(pageNum,pageSize);
        List<Role> list=roleMapper.selectRoleByKeyWord(keyWord);
        return new PageInfo<>(list);
    }

    @Override
    public void saveRole(Role role) {
        roleMapper.insertSelective(role);
    }

    @Override
    public void updateRole(Role role) {
        roleMapper.updateByPrimaryKey(role);
    }

    @Override
    public void removeRole(List<Integer> roles) {
        RoleExample roleExample=new RoleExample();
        RoleExample.Criteria criteria =roleExample.createCriteria();
        criteria.andIdIn(roles);
        roleMapper.deleteByExample(roleExample);
    }

    @Override
    public List<Role> getAssignedRole(Integer adminId) {
        return roleMapper.getAssignedRole(adminId);
    }

    @Override
    public List<Role> getUnAssignedRole(Integer adminId) {
        return roleMapper.getUnAssignedRole(adminId);
    }
}
