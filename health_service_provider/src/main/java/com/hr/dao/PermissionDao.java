package com.hr.dao;

import com.hr.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    Set<Permission> findByRoleId(Integer roleId);
}
