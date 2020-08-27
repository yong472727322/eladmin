package me.zhengjie.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * 日期工具类
 * @author leo
 * @date 2019/12/25 20:15
 */
public class DateUtil {

    /**
     * 获取当前时间
     * @return
     */
    public static Date getNow() {
        LocalDate localDate = LocalDate.now();
        return localDate2Date(localDate);
    }

    /**
     * LocalDate转Date
     * @param localDate
     * @return
     */
    public static Date localDate2Date(LocalDate localDate) {
        if(null == localDate) {
            return null;
        }
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

//    public static T

    public static void main(String[] args) {

//        // 解析日期
//        String dateText = "12/2024";
//        LocalDate date = LocalDate.parse(dateText, DateTimeFormatter.BASIC_ISO_DATE);
//        System.out.println("格式化之后的日期=" + date);
//
//        // 格式化日期
//        dateText = date.format(DateTimeFormatter.ISO_DATE);
//        System.out.println("dateText=" + dateText);

        Date date = caculateExpireDate("12/22");
        System.out.println(date);
    }

    public static Date caculateExpireDate(String exp) {
        if(null == exp){
            return null;
        }
        if(exp.contains("/")){
            String[] split = exp.trim().split("/");
            String month = split[0];
            String year = "20" + split[1];
            LocalDate of = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 01);
            return localDate2Date(of);
        }
        return null;
    }
}
