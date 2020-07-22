package com.hr.service;

import com.hr.entity.PageResult;
import com.hr.entity.QueryPageBean;
import com.hr.pojo.Setmeal;


public interface SetMealService {
    void add(Integer[] checkgroupIds, Setmeal setmeal);

    PageResult findPage(QueryPageBean queryPageBean);
}
