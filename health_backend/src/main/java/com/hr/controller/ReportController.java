package com.hr.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hr.constant.MessageConstant;
import com.hr.entity.Result;
import com.hr.service.MemberSerivce;
import com.hr.utils.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberSerivce memberSerivce;

    //会员数量折线图数据
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() throws Exception {
        Map map = new HashMap();
        //计算过去一年的12个月
        Calendar calendar = Calendar.getInstance();
        //获得当前时间往前推12个月的时间
        calendar.add(Calendar.MONTH,-12);
        List<String> monthList = new ArrayList<String>();
        Date date = null;
        for(int i = 0; i < 12;i++) {
            //获得当前时间往后推一个月的日期
            calendar.add(Calendar.MONTH, 1);
            date = calendar.getTime();
            monthList.add(new SimpleDateFormat("yyyy.MM").format(date));
        }
        map.put("months", monthList);

        List<Integer> memberCountList = memberSerivce.findMemberCountByMonths(monthList);
        map.put("memberCount",memberCountList);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }
}
