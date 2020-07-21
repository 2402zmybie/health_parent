package com.hr.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hr.constant.MessageConstant;
import com.hr.entity.PageResult;
import com.hr.entity.QueryPageBean;
import com.hr.entity.Result;
import com.hr.pojo.CheckGroup;
import com.hr.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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



    @RequestMapping("/findById.do")
    public Result findById(@RequestParam(name = "id") Integer id) {
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findCheckItemIdsByCheckGroupid")
    public Result findCheckItemIdsByCheckGroupid(@RequestParam(name = "id") Integer id) {
        try {
            List<Integer> ids = checkGroupService.findCheckItemIdsByCheckGroupid(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup, @RequestParam(name = "checkitemIds") Integer[] checkitemIds) {
        try {
            checkGroupService.eidt(checkGroup,checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/delete")
    public Result delete(@RequestParam("id") Integer id) {
        try {
            checkGroupService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }



    @RequestMapping("/findAll.do")
    public Result findAll() {
        try {
            List<CheckGroup> checkGroupList = checkGroupService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}

