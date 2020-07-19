package com.hr.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hr.dao.CheckItemDao;
import com.hr.entity.CheckItemDeleteFailException;
import com.hr.entity.PageResult;
import com.hr.entity.QueryPageBean;
import com.hr.pojo.CheckItem;
import com.hr.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 检查项服务
 */

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //完成分页查询, 基于mybatis提供的分页助手,会在sql后面拼接
        PageHelper.startPage(currentPage,pageSize);
        //where条件后面拼接 查询的关键字
        Page<CheckItem> checkItemPage =  checkItemDao.findPage(queryString);
        return new PageResult(checkItemPage.getTotal(),checkItemPage.getResult());
    }

    //处理删除检查项的逻辑
    public void deleteById(Integer id) throws CheckItemDeleteFailException {
        //先判断 检查组-检查项 关联表中有没有检查项的数据, 如果多对多的关联表中有数据,则不能删除
        Long count = checkItemDao.findCountByCheckItemid(id);
        if(count > 0) {
            //多对多的关联表中有检查项的数据
            throw new CheckItemDeleteFailException();
        }
        checkItemDao.deleteById(id);
    }
}
