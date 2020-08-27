package me.zhengjie.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EXCEL操作
 * @author leo
 * @date 2019/2/15 16:00
 */
public class ExcelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";


    /**
     * 判断Excel的版本,获取Workbook
     * @return
     * @throws IOException
     */
    private static Workbook getWorkbok(File file) throws IOException {
        try(FileInputStream in = new FileInputStream(file)){
            if(file.getName().endsWith(EXCEL_XLS)){
                try (Workbook wb = new HSSFWorkbook(in)){
                    return wb;
                }
            }else if(file.getName().endsWith(EXCEL_XLSX)){
                try (Workbook wb = new XSSFWorkbook(in)){
                    return wb;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取邮箱账号密码
     * @param emailFile
     * @return
     */
    public static List<Map<String, String>> readExcel(String emailFile) {
        Workbook workbook = null;
        List<Map<String, String>> result = new ArrayList<>();
        try{
            workbook = getWorkbok(new File(emailFile));
            if(null == workbook){
                return null;
            }
            for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
                Sheet xssfSheet = workbook.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    break;
                } else {
                    // 第一行，标题
                    Row firstRow = xssfSheet.getRow(0);
                    // 存放  序号  -->  标题
                    Map<Integer, String> titleMap = new HashMap<>();
                    if (firstRow != null) {
                        int lastCellNum = firstRow.getLastCellNum();
                        for (int i = 0; i < lastCellNum; i++) {
                            try{
                                Cell cell = firstRow.getCell(i);
                                if(null != cell){
                                    String cellValue = cell.getStringCellValue().trim();
                                    titleMap.put(i, cellValue);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                        Row xssfRow = xssfSheet.getRow(rowNum);
                        if (xssfRow != null) {
                            // 存放  标题  --->  数据
                            Map<String, String> data = new HashMap<>();
                            int lastCellNum = xssfRow.getLastCellNum();
                            for (int i = 0; i < lastCellNum; i++) {
                                try{
                                    Cell cell = xssfRow.getCell(i);
                                    if(null != cell){
                                        String title = titleMap.get(i);
                                        String cellValue;
                                        try{
                                            cellValue = cell.getStringCellValue().trim();
                                        }catch (IllegalStateException e){
                                            cellValue = String.valueOf(cell.getNumericCellValue());
                                            // 去除 "."
                                            cellValue = cellValue.split("\\.")[0];
                                            LOGGER.warn("获取第[{}]行，字段[{}]的值出错，改取数字，结果[{}]", rowNum, title, cellValue);
                                        }
                                        data.put(title, cellValue);
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            result.add(data);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null != workbook){
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    /**
     * 生成空EXCEL文件
     * @param str
     */
    public void createFile(String str) {
        FileOutputStream fileOut = null;
        try (HSSFWorkbook workbook = new HSSFWorkbook()){
            workbook.createSheet();
            fileOut = new FileOutputStream(str);
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != fileOut){
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
