package com.hr.controller;

import com.aliyuncs.exceptions.ClientException;
import com.hr.constant.MessageConstant;
import com.hr.constant.RedisMessageConstant;
import com.hr.entity.Result;
import com.hr.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    //用户在线体检预约发送验证码
    @RequestMapping("/send4Order")
    public Result send4Order(@RequestParam(name = "telephone",required = true) String telephone) {
        //设置验证码(实际开发是随机生成的4位验证码)
        String code = "1234";
        //给用户发送验证码
//        try {
//            //阿里云发送短信验证码的工具类
//            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code);
//
//        } catch (ClientException e) {
//            e.printStackTrace();
//            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
//        }
        //将验证码保存在本地   13812345678001
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER,5 * 60,code);
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);

    }
}
