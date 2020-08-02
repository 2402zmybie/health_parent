package com.hr.service;

import com.hr.pojo.User;

public interface UserService {

    User findByUserName(String username);
}
