package com.hr.controller;

import com.hr.constant.MessageConstant;
import com.hr.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getUsername")
    public Result getUsername() {
        //SpringSecurity认证和授权之后,会将用户保存在Context中, 底层是基于Session会话的
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user != null) {
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        }
        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }
}
