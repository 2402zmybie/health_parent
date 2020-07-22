package com.hr.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hr.constant.MessageConstant;
import com.hr.constant.RedisConstant;
import com.hr.entity.PageResult;
import com.hr.entity.QueryPageBean;
import com.hr.entity.Result;
import com.hr.pojo.Setmeal;
import com.hr.service.SetMealService;
import com.hr.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    //使用JedisPool操作Redis服务
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/upload")
    public Result upload(@RequestParam(name = "imgFile") MultipartFile imgFile) {
        try {
            String originalFilename = imgFile.getOriginalFilename();
            //截取后缀
            String extention = originalFilename.substring(originalFilename.lastIndexOf(".")-1);
            //图片名称不能重复, 否则覆盖了文件
            String fileName = UUID.randomUUID().toString() + extention;
            //将文件上传到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            //使用set集合 将上传的图片名称存入Redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }

    }

    @Reference
    private SetMealService setMealService;

    @RequestMapping("/add.do")
    public Result add(@RequestParam(name = "checkgroupIds") Integer[] checkgroupIds, @RequestBody Setmeal setmeal) {
        try {
            setMealService.add(checkgroupIds,setmeal);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = setMealService.findPage(queryPageBean);
        return pageResult;
    }
}
