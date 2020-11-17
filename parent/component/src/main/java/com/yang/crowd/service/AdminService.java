package com.yang.crowd.service;

import com.github.pagehelper.PageInfo;
import com.yang.crowd.entity.Admin;

import java.util.List;

public interface AdminService {

    public void save(Admin admin);

    public List<Admin> getAll();

    public Admin getAdminByLoginAcct(String loginAcct,String userPswd);

    public Admin getAdmin(String loginAcct);

    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize);

    public void remove(Integer adminId);

    public Admin getAdminById(Integer adminId);

    public void update(Admin admin);

    public void deleteByAdminIds(List<Integer> adminIds);

    public void saveAdminRoleRelationShip(Integer adminId, List<Integer> roleIdList);

    public String getPassWord(String loginAcct);
}