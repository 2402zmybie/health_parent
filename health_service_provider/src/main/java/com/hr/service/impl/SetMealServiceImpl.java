package com.hr.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hr.dao.SetMealDao;
import com.hr.pojo.Setmeal;
import com.hr.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;

    public void add(Integer[] checkgroupIds, Setmeal setmeal) {
        //插入套餐表
        setMealDao.add(setmeal);
        //得到插入套餐的id
        Integer id = setmeal.getId();
        if(checkgroupIds != null && checkgroupIds.length > 0) {
            for(Integer checkgroupId : checkgroupIds) {
                //用map封装数据
                Map map = new HashMap();
                map.put("setmeal_id",id);
                map.put("checkgroup_id",checkgroupId);
                //插入关联表
                setMealDao.addSetMealCheckGroup(map);
            }
        }

    }
}
