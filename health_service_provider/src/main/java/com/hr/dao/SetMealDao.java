package com.hr.dao;

import com.hr.pojo.Setmeal;

import java.util.Map;

public interface SetMealDao {

    void add(Setmeal setmeal);

    void addSetMealCheckGroup(Map map);
}
