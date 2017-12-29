package com.arsr.arsr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.HOUR;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.getInstance;

/**
 * 日期工具
 * Created by KundaLin on 17/12/29.
 */

class DateUtil {
    static final long MILLISECOND_OF_ONE_DAY = 1000 * 60 * 60 * 24;

    /**
     * date转为string
     *
     * @param date 日期
     * @return 字符串格式为 yyyy-MM-dd
     */
    public static String dateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static Date stringToDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(date);
    }

    /**
     * sql的date和util的date之间的转换
     * @param  d util的date
     * @return sql的date
     */
    public static java.sql.Date utilToSQL(Date d) {
        return new java.sql.Date(d.getTime());
    }

    /**
     * 获得今天日期
     * @return 今天date
     */
    public static Date getToDay() {
        Calendar c = getInstance();
        c.setTime(new Date());
        c.set(HOUR, 0);
        c.set(MINUTE, 0);
        c.set(SECOND, 0);
        c.set(MILLISECOND, 0);
        return c.getTime();
    }
}
/*public class DateUtil {
    static final long MILLISECOND_OF_ONE_DAY = 1000 * 60 * 60 * 24;


    public static Date today() {
        Calendar c = getInstance();
        c.setTime(new Date());
        c.set(HOUR, 0);
        c.set(MINUTE, 0);
        c.set(SECOND, 0);
        c.set(MILLISECOND, 0);
        return c.getTime();
    }

    public static Date monthBegin(int year, int month) {
        Calendar c = getInstance();
        c.setTime(new Date());
        c.set(YEAR, year);
        c.set(MONTH, month);
        c.set(DATE, 1);
        c.set(HOUR_OF_DAY, 0);
        c.set(MINUTE, 0);
        c.set(SECOND, 0);
        c.set(MILLISECOND, 0);
        return c.getTime();
    }

    public static Date monthEnd(int year, int month) {
        Calendar c = getInstance();
        c.setTime(new Date());
        c.set(YEAR, year);
        c.set(MONTH, month);
        c.set(HOUR, 0);
        c.set(MINUTE, 0);
        c.set(SECOND, 0);
        c.set(MILLISECOND, 0);

        c.set(DATE, 1);
        c.add(MONDAY, 1);
        c.add(DATE, -1);
        return c.getTime();
    }

    public static Date monthBegin() {
        Date today = today();
        return monthBegin(getYear(today),getMonth(today));
    }

    private static Date monthEnd() {
        Date today = today();
        return monthEnd(getYear(today),getMonth(today));
    }

    public static int daysToMonthEnd() {
        long lastDayMilliSeconds = monthEnd().getTime();
        long toDayMilliSeconds = today().getTime();
        return (int) ((lastDayMilliSeconds - toDayMilliSeconds) / MILLISECOND_OF_ONE_DAY) + 1;
    }

    private static int daysFromMonthBegin() {
        long toDayMilliSeconds = today().getTime();
        long firstDayMilliSeconds = monthBegin().getTime();
        return (int) ((toDayMilliSeconds - firstDayMilliSeconds) / MILLISECOND_OF_ONE_DAY) + 1;
    }

*//*    public static void main(String[] args) {
        System.out.println(today());
        System.out.println(monthBegin());
        System.out.println(monthEnd());
        System.out.println(daysFromMonthBegin());
        System.out.println(daysToMonthEnd());
    }*//*

    public static java.sql.Date utilToSQL(Date d) {
        return new java.sql.Date(d.getTime());
    }

    public static int getDay(Date date) {
        Calendar calendar = getInstance();
        calendar.setTime(date);
        return calendar.get(DAY_OF_MONTH);

    }

    public static int getYear(Date date) {
        Calendar calendar = getInstance();
        calendar.setTime(date);
        return calendar.get(YEAR);

    }

    public static int getMonth(Date date) {
        Calendar calendar = getInstance();
        calendar.setTime(date);
        return calendar.get(MONTH);
    }

}*/
