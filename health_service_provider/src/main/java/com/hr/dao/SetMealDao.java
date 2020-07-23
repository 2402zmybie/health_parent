package com.hr.dao;

import com.github.pagehelper.Page;
import com.hr.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealDao {

    void add(Setmeal setmeal);

    void addSetMealCheckGroup(Map map);

    Page<Setmeal> findByCondition(String queryString);

    List<Setmeal> findAll();

    Setmeal findById(int id);
}
