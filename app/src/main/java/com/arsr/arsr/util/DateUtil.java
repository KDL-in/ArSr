package com.arsr.arsr.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;

import com.arsr.arsr.MyApplication;
import com.arsr.arsr.UpdateNextDayTasksService;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.util.Calendar.HOUR;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.getInstance;

/**
 * 日期工具
 * Created by KundaLin on 17/12/29.
 */

public class DateUtil {
    static final long MILLISECOND_OF_ONE_DAY = 1000 * 60 * 60 * 24;
    private static CalendarDay calendarDay;


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

    public static Date stringToDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
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
        Calendar c = getCalendar();
        return c.getTime();
    }

    public static Calendar getCalendar() {
        Calendar c = getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        c.setTime(new Date());
        c.set(HOUR, 0);
        c.set(MINUTE, 0);
        c.set(SECOND, 0);
        c.set(MILLISECOND, 0);
        c.add(Calendar.DAY_OF_MONTH,0);
        return c;
    }


    /**
     * 返回过n天之后的日期字符串
     *
     * @param n n天之后
     */
    public static String getDateStringAfterToday(int n) {
        Calendar calendar = getCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, n);
        return dateToString(calendar.getTime());
    }

    public static String getDateStringBeforeToday(int n) {
        Calendar calendar = getCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, -n);
        return dateToString(calendar.getTime());
    }

    public static void setRepeatingTask(AppCompatActivity context, int hour, int minute, Class<UpdateNextDayTasksService> tClass) {
        Calendar calendar = Calendar.getInstance();
        long systemTime = System.currentTimeMillis();
        calendar.setTimeInMillis(systemTime);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long selectTime = calendar.getTimeInMillis();
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        LogUtil.d("开始"+getTimeStr(systemTime));
        Intent intent = new Intent(context,tClass);
        PendingIntent pendingIntent = PendingIntent.getService(context,0,intent,0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),MILLISECOND_OF_ONE_DAY,pendingIntent);
    }

    public static String getTimeStr(long time) {
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss");
        Date curDate =  new Date(time);
        return formatter.format(curDate);
    }

    public static String dateToString(CalendarDay date) {
        Date date1 = date.getDate();
        return dateToString(date1);
    }

    public static CalendarDay getCalendarDayToday() {
        Date date = getToDay();
        return CalendarDay.from(date);
    }



    public static int countDaysBetween(CalendarDay curDay, CalendarDay selDay) {
        long sub = selDay.getDate().getTime() - curDay.getDate().getTime();
        return (int) (sub/MILLISECOND_OF_ONE_DAY);
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
