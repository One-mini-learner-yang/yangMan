package com.yang.crowd.mvc.controller;

import com.yang.crowd.entity.Role;
import com.yang.crowd.service.AdminService;
import com.yang.crowd.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AssignController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("/assign/to/assign/role/page.html")
    public String toAssignRolePage(Integer adminId, ModelMap modelMap){
        List<Role> roles=roleService.getAssignedRole(adminId);
        List<Role> unRoles=roleService.getUnAssignedRole(adminId);
        modelMap.addAttribute("roles",roles);
        modelMap.addAttribute("unRoles",unRoles);
        return "assign-role";
    }

    /**
     *
     * @param adminId
     * @param pageNum
     * @param keyWord
     * @param roleIdList required=false，可以允许没有值
     * @return
     */
    @RequestMapping("/assign/do/role/assign.html")
    public String saveAdminRoleRelationShip(@RequestParam("adminId") Integer adminId,
                                            @RequestParam("pageNum") Integer pageNum,
                                            @RequestParam("keyWord") String keyWord,
                                            @RequestParam(value = "roleIdList",required = false) List<Integer> roleIdList){
        adminService.saveAdminRoleRelationShip(adminId,roleIdList);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyWord="+keyWord;
    }
}