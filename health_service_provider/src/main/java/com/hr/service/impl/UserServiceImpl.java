package com.hr.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hr.dao.PermissionDao;
import com.hr.dao.RoleDao;
import com.hr.dao.UserDao;
import com.hr.pojo.Permission;
import com.hr.pojo.Role;
import com.hr.pojo.User;
import com.hr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 用户服务
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    //根据用户名查询数据库获取用户信息和关联的角色信息,同时需要查询角色关联的权限信息
    public User findByUserName(String username) {
        //查询用户基本信息
        User user = userDao.findByUserName(username);
        if(user == null) {
            return null;
        }
        Integer userId = user.getId();
        Set<Role> roles = roleDao.findByUserId(userId);
        for (Role role : roles) {
            Integer roleId = role.getId();
            Set<Permission> permissions = permissionDao.findByRoleId(roleId);
            //让角色关联权限
            role.setPermissions(permissions);
        }
        //让用户关联角色
        user.setRoles(roles);

        return user;
    }
}
