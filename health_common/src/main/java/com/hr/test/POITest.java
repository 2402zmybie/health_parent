package com.hr.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;

public class POITest {

    /**
     * 使用POI读取Excel文件中的数据 (ordersetting_template.xlsx)
     */
    @Test
    public void test1() throws IOException {
        //获得表格(工作薄)
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("D:\\ordersetting_template.xlsx")));
        //读取excel中第一个Sheet页 (工作表)
        XSSFSheet sheet = excel.getSheetAt(0);
        for (Row row : sheet) {
            //读取行
            for (Cell cell : row) {
                //读取单元格
                System.out.println(cell.getStringCellValue());
            }
        }
        excel.close();
    }


    @Test
    public void test2() throws IOException {
        //获得表格(工作薄)
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("D:\\hellow.xlsx")));
        //读取excel中第一个Sheet页 (工作表)
        XSSFSheet sheet = excel.getSheetAt(0);
        //当前Sheet表中最后一个行号 注意:行号从0开始
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            //根据行号获取每一行
            XSSFRow row = sheet.getRow(i);
            //获取当前最后一个单元格索引
            short lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                //获取每个单元格
                XSSFCell cell = row.getCell(j);
                System.out.println(cell.getStringCellValue());
            }

        }
        excel.close();
    }


    //使用POI创建EXCEL并写入到本地磁盘中
    @Test
    public void test3() throws IOException {
        //在内存中创建一个Excel文件(工作薄)
        XSSFWorkbook excel = new XSSFWorkbook();
        //创建一个工作表对象
        XSSFSheet sheet = excel.createSheet("hr的工作表");
        for(int i = 0; i < 2;i++) {
            //在工作表中创建行对象
            XSSFRow row = sheet.createRow(i);
            //在行中创建单元格对象
            for(int j = 0; j < 2;j++) {
                XSSFCell cell = row.createCell(j);
                //给单元格中写入数据
                cell.setCellValue("我是第" + i + "行,第" + j + "个单元格");
            }
        }
        //创建输出流, 将内存中的excel输出到磁盘
        FileOutputStream out = new FileOutputStream(new File("D:\\hellow.xlsx"));
        excel.write(out);
        out.flush();
        excel.close();
    }
}
