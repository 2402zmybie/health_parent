package com.hr.jobs;

import com.hr.constant.RedisConstant;
import com.hr.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

//定时任务
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {
        System.out.println("执行定时任务方法");
        //根据redis中保存的两个set集合进行差值运算,获得垃圾图片的集合
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(set != null) {
            for (String picName : set) {
                //删除七牛云服务器上的图片
                QiniuUtils.deleteFileFromQiniu(picName);
                //从Redis集合中删除图片名称
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
                System.out.println("自定义任务执行,清理垃圾图片" + picName);
            }
        }
    }
}
