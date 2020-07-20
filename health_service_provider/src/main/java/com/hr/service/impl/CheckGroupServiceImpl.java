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
import java.util.List;
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
        setCheckGroupAndCheckItem(checkitemIds, insertGroupId);
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

    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    public List<Integer> findCheckItemIdsByCheckGroupid(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupid(id);
    }

    public void eidt(CheckGroup checkGroup, Integer[] checkitemIds) {
        //修改检查组基本信息t_checkgroup
        checkGroupDao.edit(checkGroup);
        //清理当前检查组关联的检查项, 操作中间表 t_checkgroup_checkitem
        checkGroupDao.deleteAssoication(checkGroup.getId());
        //重新建立当前检查组和检查项的关系
        Integer insertGroupId = checkGroup.getId();
        setCheckGroupAndCheckItem(checkitemIds, insertGroupId);

    }

    private void setCheckGroupAndCheckItem(Integer[] checkitemIds, Integer insertGroupId) {
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemid : checkitemIds) {
                Map map = new HashMap();
                map.put("checkgroup_id", insertGroupId);
                map.put("checkitem_id", checkitemid);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
