package com.hr.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hr.dao.MemberDao;
import com.hr.dao.OrderDao;
import com.hr.service.ReportService;
import com.hr.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    //查询运营数据
    public Map<String, Object> getBusinessReportData() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();

        //获得当前日期
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        //获得本周一的日期
        String thisWeekMonday =
                DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //获得本月第一天的日期
        String firstDay4ThisMonth =
                DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        //今日新增会员数
        Integer todayNewMember = memberDao.findMemberCountByDate(today);
        //总会员数
        Integer totalMember = memberDao.findMemberTotalCount();
        //本周新增会员数
        Integer thisWeekNewMember =
                memberDao.findMemberCountAfterDate(thisWeekMonday);
        //本月新增会员数
        Integer thisMonthNewMember =
                memberDao.findMemberCountAfterDate(firstDay4ThisMonth);
        //今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
        //本周预约数
        Integer thisWeekOrderNumber =
                orderDao.findOrderCountAfterDate(thisWeekMonday);
        //本月预约数
        Integer thisMonthOrderNumber =
                orderDao.findOrderCountAfterDate(firstDay4ThisMonth);
        //今日到诊数
        Integer todayVisitsNumber =
                orderDao.findVisitsCountByDate(today);

        //本周到诊数
        Integer thisWeekVisitsNumber =
                orderDao.findVisitsCountAfterDate(thisWeekMonday);
        //本月到诊数
        Integer thisMonthVisitsNumber =
                orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);
        //热门套餐（取前4）
        List<Map> hotSetmeal = orderDao.findHotSetmeal();
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("reportDate",today);
        result.put("todayNewMember",todayNewMember);
        result.put("totalMember",totalMember);
        result.put("thisWeekNewMember",thisWeekNewMember);
        result.put("thisMonthNewMember",thisMonthNewMember);
        result.put("todayOrderNumber",todayOrderNumber);
        result.put("thisWeekOrderNumber",thisWeekOrderNumber);
        result.put("thisMonthOrderNumber",thisMonthOrderNumber);
        result.put("todayVisitsNumber",todayVisitsNumber);
        result.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        result.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        result.put("hotSetmeal",hotSetmeal);
        return result;
    }
}
