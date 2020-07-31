package com.hr.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.hr.constant.MessageConstant;
import com.hr.constant.RedisMessageConstant;
import com.hr.entity.Result;
import com.hr.pojo.Member;
import com.hr.service.MemberSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 处理会员相关操作
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberSerivce memberSerivce;

    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        //从Redis中获取保存的验证码 和提交过来的比对
        String validateCodeRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        String validateCode = (String) map.get("validateCode");
        if(validateCode !=null && validateCodeRedis!=null && validateCode.equals(validateCodeRedis)) {
            //输入正确
            //判断当前用户是否是会员
            Member member = memberSerivce.findByTelephone(telephone);
            if(member == null) {
                //不是会员 自动完成注册(自动将会员信息保存到会员表)
                member = new Member();
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberSerivce.addMember(member);
            }
            //向客户端浏览器写入Cookie,内容为手机号(目的是跟踪用户, 一旦登录的时候写入cookie,那么后面所有的请求都会携带cookie)
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);  //有效期30天
            response.addCookie(cookie);
            //将会员信息保存到Redis (登录成功后, 在redis中存储登录信息,而不使用session,原因是session在集群中表现不佳)
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone, 60*30,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }
}
