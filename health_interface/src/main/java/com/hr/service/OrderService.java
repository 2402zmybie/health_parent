package com.hr.service;

import com.hr.entity.Result;

import java.util.Map;

public interface OrderService {

    public Result order(Map map) throws Exception;

    Map findById(Integer id) throws Exception;
}
