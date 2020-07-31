package com.hr.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hr.constant.MessageConstant;
import com.hr.dao.MemberDao;
import com.hr.dao.OrderDao;
import com.hr.dao.OrderSettingDao;
import com.hr.entity.Result;
import com.hr.pojo.Member;
import com.hr.pojo.Order;
import com.hr.pojo.OrderSetting;
import com.hr.service.OrderService;
import com.hr.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MemberDao memberDao;

    //体检预约
    public Result order(Map map) throws Exception {
        //检查当前日期是否进行了预约设置
        String orderDate = (String) map.get("orderDate");
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(DateUtils.parseString2Date(orderDate));
        if(orderSetting == null) {
            //所选日期不能预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //检查预约日期是否预约已满
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if(reservations >= number) {
            //已经约满
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //检查用户是否重复预约
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if(member != null) {
            //判断是否重复预约
            Integer memberId = member.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order = new Order(memberId,DateUtils.parseString2Date(orderDate),null,null,setmealId);
            List<Order> list = orderDao.findByCondition(order);
            if(list != null && list.size() > 0){
                //已经完成了预约，不能重复预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }else {
            //不是会员 自动完成注册
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }

        //保存预约信息到预约表
        Order order = new Order(member.getId(),
                DateUtils.parseString2Date(orderDate), (String)map.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);
        //可以预约，设置预约人数加一
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());

    }

    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        //处理日期格式
        if(map != null) {
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
