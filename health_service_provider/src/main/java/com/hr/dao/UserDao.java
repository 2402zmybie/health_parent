package com.hr.dao;

import com.hr.pojo.User;

public interface UserDao {

    User findByUserName(String username);
}
