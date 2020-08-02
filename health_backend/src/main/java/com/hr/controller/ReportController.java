package com.hr.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hr.constant.MessageConstant;
import com.hr.entity.Result;
import com.hr.service.MemberSerivce;
import com.hr.service.ReportService;
import com.hr.service.SetMealService;
import com.hr.utils.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberSerivce memberSerivce;
    @Reference
    private SetMealService setMealService;

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


    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() throws Exception {
        Map map = new HashMap();
        try {
            List<Map<String,Object>> setmealCountList = setMealService.findSetmealCount();
            map.put("setmealCount",setmealCountList);

            List<String> setMealNames = new ArrayList<String>();
            for (Map<String, Object> objectMap : setmealCountList) {
                String name = (String) objectMap.get("name");
                setMealNames.add(name);
            }
            map.put("setmealNames", setMealNames);

            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @Reference
    private ReportService reportService;

    //获得运营数据
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String,Object> data = reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    //导出Excel文件
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String,Object> result = reportService.getBusinessReportData();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer)
                    result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer)
                    result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer)
                    result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer)
                    result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer)
                    result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer)
                    result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer)
                    result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //获取服务器excel模板文件的绝对路径
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            //基于模板文件在内存中创建一个Excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filePath));
            //读取第一个工作表
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数
            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数
            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数
            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数
            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数
            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }


            //使用输出流进行表格下载,基于浏览器作为客户端下载
            OutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");//代表的是Excel文件类型
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//指定以附件形式进行下载
            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }
}
