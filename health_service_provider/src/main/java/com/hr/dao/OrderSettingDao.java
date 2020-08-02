package com.hr.dao;

import com.hr.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    void add(OrderSetting orderSetting);
    void editNumberByOrderDate(OrderSetting orderSetting);
    long findCountByOrderDate(Date orderDate);

    List<OrderSetting> getOrderSettingByMonth(Map map);

    OrderSetting findByOrderDate(Date parseString2Date);

    void editReservationsByOrderDate(OrderSetting orderSetting);
}
