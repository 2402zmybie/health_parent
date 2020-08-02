package com.hr.dao;

import com.hr.pojo.Role;

import java.util.Set;

public interface RoleDao {
    Set<Role> findByUserId(Integer userId);
}
