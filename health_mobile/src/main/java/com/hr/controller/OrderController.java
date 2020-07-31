package com.hr.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hr.constant.MessageConstant;
import com.hr.constant.RedisMessageConstant;
import com.hr.entity.Result;
import com.hr.pojo.Order;
import com.hr.service.OrderService;
import com.hr.utils.SMSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        //从Redis中获取保存的验证码
        String telephone = (String) map.get("telephone");
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");
        if(validateCode != null &&validateCodeInRedis != null && validateCode.equals(validateCodeInRedis)) {
            //用户输入的验证码和redis中相同
            //调用服务完成预约业务处理
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            Result result = null;
            try {
                result = orderService.order(map);
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
            if(result.isFlag()) {
                //预约成功, 可以给用户发送短信
//                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone, (String) map.get("orderDate"));
                log.error("预约成功,发送短信.....");
            }
            return result;
        }else {
            //验证码比对不成功
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }


    @RequestMapping("/findById")
    public Result findById(@RequestParam(name = "id",required = true) Integer id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
