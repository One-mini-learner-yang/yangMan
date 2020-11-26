package com.yang.crowd.mvc.interception;

import com.yang.crowd.constant.CrowdConstant;
import com.yang.crowd.entity.Admin;
import com.yang.crowd.exception.AccessForBiddenException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session=request.getSession();
        Admin admin= (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);
        if (admin==null){
            request.setAttribute("exception",new AccessForBiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDEN));
            request.getRequestDispatcher("/WEB-INF/admin-login.jsp").forward(request,response);
        }
        return true;
    }
}