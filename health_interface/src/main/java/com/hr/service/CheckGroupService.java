package com.hr.service;

import com.hr.entity.PageResult;
import com.hr.entity.QueryPageBean;
import com.hr.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    void add(CheckGroup checkGroup,Integer[] checkitemIds);

    PageResult findPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupid(Integer id);

    void eidt(CheckGroup checkGroup, Integer[] checkitemIds);
}
