package com.yang.crowd.mvc.config;

import com.yang.crowd.CrowdConstant;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class SystemLogOutFilter extends LogoutFilter {
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Logger logger= LoggerFactory.getLogger(getClass());
        HttpServletRequest httpServletRequest= (HttpServletRequest) request;
        HttpSession session=httpServletRequest.getSession();
        if (session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN)!=null){
            session.removeAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);
        }
        Subject subject=getSubject(request,response);
        String redirectUrl=getRedirectUrl(request,response,subject);
        try{
            subject.logout();
        }catch (Exception e){
            e.printStackTrace();
        }
        return super.preHandle(httpServletRequest,response);
    }
}