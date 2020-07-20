package com.hr.dao;
import com.github.pagehelper.Page;
import com.hr.pojo.CheckItem;

public interface CheckItemDao {
    void add(CheckItem checkItem);

    Page<CheckItem> findPage(String queryString);

    Long findCountByCheckItemid(Integer id);

    void deleteById(Integer id);

    void edit(CheckItem checkItem);

    CheckItem findById(Integer id);
}
