package com.hr.service;

import com.hr.entity.PageResult;
import com.hr.entity.QueryPageBean;
import com.hr.pojo.Setmeal;

import java.util.List;
import java.util.Map;


public interface SetMealService {
    void add(Integer[] checkgroupIds, Setmeal setmeal);

    PageResult findPage(QueryPageBean queryPageBean);

    List<Setmeal> findAll();

    Setmeal findById(int id);

    List<Map<String, Object>> findSetmealCount();

}
