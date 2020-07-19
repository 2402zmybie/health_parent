package com.hr.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.hr.constant.MessageConstant;
import com.hr.entity.PageResult;
import com.hr.entity.QueryPageBean;
import com.hr.entity.Result;
import com.hr.pojo.CheckItem;
import com.hr.service.CheckItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}