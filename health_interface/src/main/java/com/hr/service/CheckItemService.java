package com.hr.service;

import com.hr.entity.PageResult;
import com.hr.entity.QueryPageBean;
import com.hr.pojo.CheckItem;

public interface CheckItemService {


    void add(CheckItem checkItem);

    PageResult findPage(QueryPageBean queryPageBean);
}
