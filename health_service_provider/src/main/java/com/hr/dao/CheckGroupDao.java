package com.hr.dao;
import com.github.pagehelper.Page;
import com.hr.pojo.CheckGroup;
import com.hr.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map map);

    Page<CheckGroup> findByCondition(@Param(value = "queryString") String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupid(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteAssoication(Integer id);


    void deleteGroupById(Integer id);

    List<CheckGroup> findAll();
}
