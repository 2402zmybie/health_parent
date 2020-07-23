package com.hr.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hr.constant.RedisConstant;
import com.hr.dao.SetMealDao;
import com.hr.entity.PageResult;
import com.hr.entity.QueryPageBean;
import com.hr.pojo.Setmeal;
import com.hr.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;
    @Autowired
    private JedisPool jedisPool;

    public void add(Integer[] checkgroupIds, Setmeal setmeal) {
        //插入套餐表
        setMealDao.add(setmeal);
        //得到插入套餐的id
        Integer id = setmeal.getId();
        setMealAndCheckGroup(checkgroupIds, id);
        //将图片名称保存到Redis集合中
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);

    }

    //分页查询所有的套餐
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> setmealPage =  setMealDao.findByCondition(queryString);
        return new PageResult(setmealPage.getTotal(),setmealPage.getResult());
    }


    private void setMealAndCheckGroup(Integer[] checkgroupIds, Integer id) {
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

    public List<Setmeal> findAll() {
        return setMealDao.findAll();
    }
}
