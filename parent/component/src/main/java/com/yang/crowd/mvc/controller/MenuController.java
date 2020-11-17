package com.yang.crowd.mvc.controller;

import com.yang.crowd.entity.Menu;
import com.yang.crowd.service.MenuService;
import com.yang.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MenuController {
    @Autowired
    private MenuService menuService;

    @ResponseBody
    @RequestMapping("/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTreeNew(){
        List<Menu> menuList=menuService.getAll();
        Menu root=null;
        Map<Integer,Menu> menuMap=new HashMap<>();
        for (Menu menu:menuList){
            menuMap.put(menu.getId(),menu);
        }
        for(Menu menu:menuList){
            if(menu.getPid()==null){
                root=menu;
                continue;
            }
            Menu father=menuMap.get(menu.getPid());
            father.getChildren().add(menu);
        }
        return ResultEntity.successWithData(root);
    }
    @ResponseBody
    @RequestMapping("/menu/save.json")
    public ResultEntity<String> saveMenu(Menu menu){
        menuService.saveMenu(menu);
        return ResultEntity.successWithoutData();
    }
    @ResponseBody
    @RequestMapping("/menu/update.json")
    public ResultEntity<String> updateMenu(Menu menu){
        System.out.println(menu.getName());
        menuService.updateMenu(menu);
        return ResultEntity.successWithoutData();
    }
    @ResponseBody
    @RequestMapping("/menu/remove.json")
    public ResultEntity<String> deleteMenu(Integer id){
        menuService.deleteMenu(id);
        return ResultEntity.successWithoutData();
    }
}
