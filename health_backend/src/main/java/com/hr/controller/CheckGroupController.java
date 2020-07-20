package com.hr.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hr.constant.MessageConstant;
import com.hr.entity.PageResult;
import com.hr.entity.QueryPageBean;
import com.hr.entity.Result;
import com.hr.pojo.CheckGroup;
import com.hr.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, @RequestParam(name = "checkitemIds") Integer[] checkitemIds) {
        try {
            checkGroupService.add(checkGroup,checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkGroupService.findPage(queryPageBean);
        return pageResult;
    }
}

