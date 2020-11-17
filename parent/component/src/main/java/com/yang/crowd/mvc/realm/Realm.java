package com.yang.crowd.mvc.realm;

import com.yang.crowd.service.AdminService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class Realm extends AuthorizingRealm {
    @Autowired
    private AdminService adminService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken= (UsernamePasswordToken) authenticationToken;
        String passWord=adminService.getPassWord(usernamePasswordToken.getUsername());
        SimpleAuthenticationInfo simpleAuthenticationInfo=new SimpleAuthenticationInfo(usernamePasswordToken.getUsername(),passWord,getName());
        return simpleAuthenticationInfo;
    }
}
