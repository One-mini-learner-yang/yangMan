package com.yang.crowd.mvc.controller;

import com.github.pagehelper.PageInfo;
import com.yang.crowd.CrowdConstant;
import com.yang.crowd.entity.Admin;
import com.yang.crowd.exception.LoginFailedException;
import com.yang.crowd.service.AdminService;
import com.yang.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;

    @RequestMapping("/admin/do/login.html")
    public String doLogin(String loginAcct, String userPswd, HttpSession session){
//        Admin admin=adminService.getAdminByLoginAcct(loginAcct,userPswd);
//        log.info(admin.toString());
//        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN,admin);
//        return "redirect:/admin/to/main/page.html";
        Subject subject= SecurityUtils.getSubject();
        if (!subject.isAuthenticated()){
            UsernamePasswordToken token=new UsernamePasswordToken(loginAcct,userPswd);
            try{
                Admin admin=adminService.getAdmin(loginAcct);
                session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN,admin);
                subject.login(token);
            }catch (Exception e){
                e.printStackTrace();
                session.invalidate();
                throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
            }
        }
        return "redirect:/admin/to/main/page.html";
    }
    @RequestMapping("/admin/do/logout.html")
    public String doLogOut(HttpSession session){
        return "redirect:/admin/to/login/page.html";
    }
    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(@RequestParam(value = "keyWord",defaultValue = "") String keyWord,
                              @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
                              ModelMap modelMap){
        PageInfo<Admin> pageInfo=adminService.getPageInfo(keyWord,pageNum,pageSize);
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO,pageInfo);
        return "admin-page";

    }
    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyWord}.html")
    public String remove(@PathVariable("adminId")Integer adminId,
                         @PathVariable("pageNum")Integer pageNum,
                         @PathVariable("keyWord")String  keyWord,
                         HttpSession session){
        Admin admin= (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);
        if (adminId.equals(admin.getId())){
            throw new RuntimeException(CrowdConstant.MESSAGE_REMOVE_USER_SELF);
        }
        adminService.remove(adminId);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyWord="+keyWord;
    }
    @RequestMapping("/admin/save.html")
    public String save(Admin admin){
        adminService.save(admin);
        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }
    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(@RequestParam("adminId") Integer adminId,
                             @RequestParam("pageNum") Integer pageNum,
                             @RequestParam("keyWord") String keyWord,
                             ModelMap modelMap){
        Admin admin=adminService.getAdminById(adminId);
        modelMap.addAttribute("admin",admin);
        return "admin-edit";
    }
    @RequestMapping("/admin/update.html")
    public String update(Admin admin,
                         @RequestParam("pageNum") Integer pageNum,
                         @RequestParam("keyWord") String keyWord){
        adminService.update(admin);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyWord="+keyWord;
    }
    @ResponseBody
    @RequestMapping("/admin/delete/by/admin/id/array.json")
    public ResultEntity<String> deleteByAdminId(@RequestBody List<Integer> adminIds){
        adminService.deleteByAdminIds(adminIds);
        return ResultEntity.successWithoutData();
    }
}