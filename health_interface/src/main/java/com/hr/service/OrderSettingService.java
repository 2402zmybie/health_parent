package com.hr.service;

import com.hr.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {


    void add(List<OrderSetting> orderSettings);

    List<Map> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);
}
