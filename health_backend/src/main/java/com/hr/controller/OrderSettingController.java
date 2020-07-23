package com.hr.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.hr.constant.MessageConstant;
import com.hr.entity.Result;
import com.hr.pojo.OrderSetting;
import com.hr.service.OrderSettingService;
import com.hr.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    //得到前端上传的Excel文件
    @RequestMapping("/upload.do")
    public Result upload(@RequestParam(name = "excelFile") MultipartFile excelFile) {
        //使用POI解析表格数据
        try {
            List<String[]> rows = POIUtils.readExcel(excelFile);
            List<OrderSetting> orderSettings = new ArrayList<OrderSetting>();
            if(rows !=null && rows.size() > 0) {
                OrderSetting orderSetting = null;
                for (String[] row : rows) {
                    String date = row[0];
                    String number = row[1];
                    orderSetting = new OrderSetting(new Date(date),Integer.parseInt(number));
                    orderSettings.add(orderSetting);
                }
                //通过dubbo远程调用服务实现数据批量导入到数据库
                orderSettingService.add(orderSettings);
            }
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/getOrderSettingByMonth.do")
    public Result getOrderSettingByMonth(@RequestParam(name = "date",required = true) String date) {
        // date格式为 yyyy-MM
        try {
            //返回值包装 前端需要的形式
            List<Map> lists =  orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,lists);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
