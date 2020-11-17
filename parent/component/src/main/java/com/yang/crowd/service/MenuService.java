package com.yang.crowd.service;

import com.yang.crowd.entity.Menu;

import java.util.List;

public interface MenuService {
    public List<Menu> getAll();
    public void saveMenu(Menu menu);
    public void updateMenu(Menu menu);
    public void deleteMenu(Integer id);
}
