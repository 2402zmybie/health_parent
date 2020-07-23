package com.hr.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hr.dao.OrderSettingDao;
import com.hr.pojo.OrderSetting;
import com.hr.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    //批量导入预约设置数据
    public void add(List<OrderSetting> orderSettings) {
        if(orderSettings != null && orderSettings.size() > 0) {
            for (OrderSetting orderSetting : orderSettings) {
                //查询当天是不是有预约人数
                long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if(countByOrderDate > 0) {
                    //当天有预约人数, 那么更新数据
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    //当天没有预约数据, 那么插入
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    public List<Map> getOrderSettingByMonth(String date) {
        String begin = date + "-1";
        String end = date + "-31";
        Map map = new HashMap();
        map.put("begin",begin);
        map.put("end",end);
        List<OrderSetting> list =  orderSettingDao.getOrderSettingByMonth(map);
        List<Map> result = new ArrayList<Map>();
        if(list != null && list.size() > 0) {
            Map resultMap = null;
            for (OrderSetting orderSetting : list) {
                 resultMap = new HashMap();
                 //获取日期数字, 几号
                 resultMap.put("date",orderSetting.getOrderDate().getDate());
                 resultMap.put("number",orderSetting.getNumber());
                 resultMap.put("reservations",orderSetting.getReservations());
                 result.add(resultMap);
            }
        }
        return result;
    }

    public void editNumberByDate(OrderSetting orderSetting) {
        //先根据日期查询是否已经进行了预约设置
        long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(countByOrderDate > 0) {
            //当前日期已经进行了预约设置, 执行更新操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            //当前日期没有进行了预约设置, 执行插入
            orderSettingDao.add(orderSetting);
        }

    }
}
