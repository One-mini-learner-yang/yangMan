package com.yang.crowd.mvc.config;

import com.google.gson.Gson;
import com.yang.crowd.exception.LoginAcctAlreadyInUseException;
import com.yang.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.yang.crowd.exception.LoginFailedException;
import com.yang.crowd.util.CrowdUtil;
import com.yang.crowd.util.ResultEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class CrowdExceptionResolver {
    @ExceptionHandler({NullPointerException.class})
    public ModelAndView resolveNullPointerException(NullPointerException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return commonResolve("system-error",exception,response,request);
    }
    @ExceptionHandler(ArithmeticException.class)
    public ModelAndView resolveArithmeticException(ArithmeticException exception,HttpServletRequest request,HttpServletResponse response) throws IOException {
        return commonResolve("system-error",exception,response,request);
    }
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView resolveRunTimeException(RuntimeException exception,HttpServletRequest request,HttpServletResponse response) throws IOException {
        return commonResolve("system-error",exception,response,request);
    }
    @ExceptionHandler(LoginFailedException.class)
    public  ModelAndView resolveLoginFailedException(LoginFailedException exception, HttpServletResponse response, HttpServletRequest request) throws IOException {
        return commonResolve("admin-login",exception,response,request);
    }
    @ExceptionHandler(LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(LoginAcctAlreadyInUseException exception,HttpServletResponse response,HttpServletRequest request) throws IOException {
        return commonResolve("admin-add",exception,response,request);
    }
    @ExceptionHandler(LoginAcctAlreadyInUseForUpdateException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseForUpdateException(LoginAcctAlreadyInUseForUpdateException exception, HttpServletResponse response, HttpServletRequest request) throws IOException {
        return commonResolve("system-error",exception,response,request);
    }
    private  ModelAndView commonResolve(String viewName,Exception exception,HttpServletResponse response,HttpServletRequest request) throws IOException {
        boolean judge= CrowdUtil.judgeRequestType(request);
        if (judge){
            ResultEntity<Object> resultEntity=ResultEntity.failed(exception.getMessage());
            Gson gson=new Gson();
            String json=gson.toJson(resultEntity);
            response.getWriter().write(json);
            return null;
        }
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("exception",exception);
        modelAndView.setViewName(viewName);
        return modelAndView;
    }
}