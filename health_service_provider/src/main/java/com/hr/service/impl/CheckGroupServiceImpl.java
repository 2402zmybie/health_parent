package com.hr.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hr.dao.CheckGroupDao;
import com.hr.entity.PageResult;
import com.hr.entity.QueryPageBean;
import com.hr.pojo.CheckGroup;
import com.hr.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //先插入t_checkgroup表中的数据
        checkGroupDao.add(checkGroup);
        //得到插入数据的id
        Integer insertGroupId = checkGroup.getId();
        //插入多对多的关联表
        if(checkitemIds != null && checkitemIds.length > 0) {
            for(Integer checkitemid : checkitemIds) {
                Map map = new HashMap();
                map.put("checkgroup_id",insertGroupId);
                map.put("checkitem_id",checkitemid);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    //分页查询检查组
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> groupPage = checkGroupDao.findByCondition(queryString);
        return new PageResult(groupPage.getTotal(),groupPage.getResult());
    }
}
