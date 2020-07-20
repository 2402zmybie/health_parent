package com.hr.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.hr.constant.MessageConstant;
import com.hr.entity.CheckItemDeleteFailException;
import com.hr.entity.PageResult;
import com.hr.entity.QueryPageBean;
import com.hr.entity.Result;
import com.hr.pojo.CheckItem;
import com.hr.service.CheckItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    //  @Reference 查找服务
    @Reference
    private CheckItemService checkItemService;

    // RequestBody注解是把传过来的json对象封装成为实体类
    @RequestMapping("/add.do")
    public Result add(@RequestBody CheckItem checkItem) {
        //ctrl+alt+t  try-catch快捷键
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        //添加数据没有其他显示的, 只需要成功失败信息即可
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkItemService.findPage(queryPageBean);
        return pageResult;
    }

    @RequestMapping("/delete.do")
    public Result delete(@RequestParam(name = "id",required = true) Integer id) {
        try {
            checkItemService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof CheckItemDeleteFailException) {
                return new Result(false,"已经有检查组关联此检查项,请先删除检查组后再操作");
            }
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(false, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }



    @RequestMapping("/findById")
    public Result findById(@RequestParam(name = "id",required = true) Integer id) {
        try {
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/edit.do")
    public Result eidt(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.edit(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }
}
