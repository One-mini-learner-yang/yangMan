package com.yang.crowd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yang.crowd.constant.CrowdConstant;
import com.yang.crowd.entity.Admin;
import com.yang.crowd.entity.AdminExample;
import com.yang.crowd.exception.LoginAcctAlreadyInUseException;
import com.yang.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.yang.crowd.exception.LoginFailedException;
import com.yang.crowd.mapper.AdminMapper;
import com.yang.crowd.service.AdminService;
import com.yang.crowd.util.CrowdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {


    @Autowired
    private AdminMapper adminMapper;
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public void save(Admin admin) {
        String userPswd=admin.getUserPswd();
        admin.setUserPswd(CrowdUtil.md5(userPswd));
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        admin.setCreateTime(dateFormat.format(new Date()));
        try {
            adminMapper.insert(admin);
        }catch (Exception e){
            if (e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }

    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    /**
     * 数据库根据账号查询Admin对象=>判断是否为空（为空抛自定义异常）
     * =>不为空取出Admin=>比较密码是否正确（加密后）
     * =>不一致抛异常，一致返回Admin
     * @param loginAcct  账号
     * @param userPswd   密码
     * @return
     */
    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        AdminExample adminExample=new AdminExample();
        AdminExample.Criteria criteria=adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(loginAcct);
        List<Admin> adminList = adminMapper.selectByExample(adminExample);
        if (adminList==null||adminList.size()==0){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        if (adminList.size()>1){
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
        Admin admin=adminList.get(0);
        if(admin==null){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        String userPswDB=admin.getUserPswd();
        String userPswForm= CrowdUtil.md5(userPswd);
        if (!Objects.equals(userPswDB,userPswForm)){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        return admin;
    }

    @Override
    public Admin getAdmin(String loginAcct) {
        AdminExample adminExample=new AdminExample();
        AdminExample.Criteria criteria=adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(loginAcct);
        return adminMapper.selectByExample(adminExample).get(0);
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        //非侵入式设计
        PageHelper.startPage(pageNum,pageSize);
        List<Admin> adminList=adminMapper.selectAdminByKeyWord(keyword);

        return new PageInfo<>(adminList);
    }

    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void update(Admin admin) {
        try{
            adminMapper.updateByPrimaryKeySelective(admin);
        }catch (Exception e){
            if (e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }

    }

    @Override
    public void deleteByAdminIds(List<Integer> adminIds) {
        AdminExample adminExample=new AdminExample();
        AdminExample.Criteria criteria=adminExample.createCriteria();
        criteria.andIdIn(adminIds);
        adminMapper.deleteByExample(adminExample);
    }

    @Override
    public void saveAdminRoleRelationShip(Integer adminId, List<Integer> roleIdList) {
        adminMapper.deleteRelationShip(adminId);
        if (roleIdList!=null&&roleIdList.size()!=0){
            adminMapper.insertNewRelationShip(adminId,roleIdList);
        }
    }

    @Override
    public String getPassWord(String loginAcct) {
        AdminExample adminExample=new AdminExample();
        AdminExample.Criteria criteria=adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(loginAcct);
        String pswd=adminMapper.selectByExample(adminExample).get(0).getUserPswd();
        return pswd;
    }
}