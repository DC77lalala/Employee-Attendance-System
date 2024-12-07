package com.wdc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class DateUtil {

    // 获取当前日期，格式为 "yyyy-MM-dd"
    public static String now() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    //仅返回当前日期的方法（不带时间），可以保留或重新命名您原先的方法
    public static String today() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    // 将字符串解析为 Date 对象，根据给定的日期格式
    public static Date parse(String dateStr, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(dateStr);
    }

    // 计算两个日期之间的天数差，包含起始日期和结束日期为一天的情况
    public static long betweenDay(Date endDate, Date startDate, boolean includeEndDate) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(endDate);
        cal2.setTime(startDate);

        long between = 0;
        while (cal1.after(cal2) || (includeEndDate && cal1.equals(cal2))) {
            cal2.add(Calendar.DATE, 1);
            between++;
        }
        return between - 1; // 减去最后一天多加的1天，如果不包含结束日期则不需要减
    }

    // 如果需要包含结束日期在内的计算（即直接的天数差），可以提供一个简化方法
    public static long betweenDayInclusive(Date endDate, Date startDate) {
        return betweenDay(endDate, startDate, true);
    }

    // 如果不需要包含结束日期在内的计算（即标准的天数差），可以提供一个简化方法
    public static long betweenDayExclusive(Date endDate, Date startDate) {
        return betweenDay(endDate, startDate, false);
    }

    // 主方法用于测试（可选）
    public static void main(String[] args) throws ParseException {
        String today = today();
        System.out.println("Today: " + today);

        String now = now();
        System.out.println("now: " + now);

        Date date1 = parse("2023-10-01", "yyyy-MM-dd");
        Date date2 = parse("2023-10-10", "yyyy-MM-dd");

        System.out.println("Days between (inclusive): " + betweenDayInclusive(date2, date1));
        System.out.println("Days between (exclusive): " + betweenDayExclusive(date2, date1));
    }
}