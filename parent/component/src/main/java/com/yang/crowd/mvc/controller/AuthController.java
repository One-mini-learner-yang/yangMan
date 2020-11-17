package com.yang.crowd.mvc.controller;

import com.yang.crowd.entity.Auth;
import com.yang.crowd.service.AuthService;
import com.yang.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;
    @RequestMapping({"/auth/get/whole/tree.json","/assign/get/all/auth.json"})
    public ResultEntity<Auth> getWholeTreeNew(){
        List<Auth> auths=authService.getAll();
        Auth root=null;
        Map<Integer,Auth> map=new HashMap<>();
        for (Auth auth:auths){
            map.put(auth.getId(),auth);
        }
        for (Auth auth:auths){
            if (auth.getCategoryId()==null){
                root=auth;
                continue;
            }
            Auth father=map.get(auth.getCategoryId());
            father.getChildren().add(auth);
        }
        return ResultEntity.successWithData(root);
    }
    @RequestMapping("/assign/get/assign/auth/id/by/role/id.json")
    public ResultEntity<List<Integer>> getAssignedAuthByRoleId(Integer roleId){
        List<Integer> ids=authService.getAssignedAuthByRoleId(roleId);
        return ResultEntity.successWithData(ids);
    }
    @RequestMapping("assign/do/role/assign/auth.json")
    public ResultEntity<String> saveRoleAuthRelationship(@RequestBody Map<String,List<Integer>> map){
        authService.saveRoleAuthRelationship(map);
        return ResultEntity.successWithoutData();
    }
}
