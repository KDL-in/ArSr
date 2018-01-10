package com.arsr.arsr.util;


import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.arsr.arsr.MyApplication;

import com.arsr.arsr.db.Category;
import com.arsr.arsr.db.Tag;
import com.arsr.arsr.db.Task;
import com.arsr.arsr.db.dao.CategoryDAO;
import com.arsr.arsr.db.dao.TagDAO;
import com.arsr.arsr.db.dao.TaskDAO;
import com.arsr.arsr.db.DataBaseHelper;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.File;
import java.util.List;

import drawthink.expandablerecyclerview.bean.RecyclerViewData;

/**
 * 数据库操作工具
 * 用于对接数据库，存取数据
 * Created by KundaLin on 17/12/29.
 */

public class DBUtil {
    public final static String DELETED = "beDeleted";

    /**
     * 用于初始化数据库
     */
    private static DataBaseHelper helper;
    public static SQLiteDatabase db;
    public static CategoryDAO categoryDAO;
    public static TagDAO tagDAO;
    public static TaskDAO taskDAO;

    static {
        helper = new DataBaseHelper(MyApplication.getContext(), "ArSrDB.db", null, 1);
        db = helper.getWritableDatabase();
        categoryDAO = new CategoryDAO();
        tagDAO = new TagDAO();
        taskDAO = new TaskDAO();
        //数据更新
        updateData();
    }

    /**
     * 更新任务执行数据
     */
    public static void updateData() {
        //获取更新标志（sp）
        CalendarDay lastReco = IOUtil.getUpdateRecord();
        CalendarDay lastExec = IOUtil.getLastExec();
        CalendarDay today = DateUtil.getCalendarDayToday();
        LogUtil.d(lastReco.toString());
        LogUtil.d(lastExec.toString());
        LogUtil.d(today.toString());
        if (!lastReco.equals(lastExec)&&!lastExec.equals(today)) {//最后执行时间就和最后记录更新时间不匹配
                                                                //且最后执行时间不为今天
            String str = DateUtil.dateToString(lastExec);
//            String str = DateUtil.getDateStringAfterToday(0);
            LogUtil.d("if (!lastDate.equals(today))");
            List<Task> list = DBUtil.taskDAO.getList();
            //检测是否所有任务都执行完
            for (Task t :
                    list) {
                if (t.getDayToRecall() == 0 || t.getDayToAssist() == 0) return;
            }
            LogUtil.d("IOUtil.clearAdjustRecords();");
            //清空操作记录
            IOUtil.clearAdjustRecords();
            for (Task t :
                    list) {
                boolean ifBeExec = false;
                int rPit[] = IOUtil.getPointsInTimeOf(t.getTid()), aPit[] = IOUtil.getPointsInTimeOfAssist();
                if (t.getDayToRecall() == -1 && t.getTimes() < 6) {
                    t.setDayToRecall(rPit[t.getTimes()]);
                    t.setTimes(t.getTimes() + 1);
                    //辅助重置
                    t.setDayToAssist(-2);
                    t.setAssistTimes(-2);
                    ifBeExec = true;
                }
                if (t.getDayToAssist() == -1 && t.getAssistTimes() < 4) {
                    t.setDayToAssist(aPit[t.getAssistTimes()]);
                    t.setAssistTimes(t.getAssistTimes() + 1);
                    ifBeExec = true;
                }
                if (ifBeExec) {
                    //保存记录
                    IOUtil.saveRecallRecord(str, t);
                }
                if (t.getDayToRecall() != -1) t.setDayToRecall(t.getDayToRecall() - 1);
                if (t.getDayToAssist() != -2) t.setDayToAssist(t.getDayToAssist() - 1);
                //更新
                DBUtil.taskDAO.update(t);
            }
            IOUtil.setUpdateRecord(str);
            categoryDAO.display();
            tagDAO.display();
            taskDAO.display();
        }
    }


    /**
     * 解析出名字中类别，名字，编码
     *
     * @param name 格式为 category_name_number
     * @return 返回解析后的数组
     */
    public static String[] parse(String name) {
        return name.split("_");
    }

    /**
     * 获得任务实体
     */
    public static Task getTask(String name) {
        return taskDAO.get(name);
    }

    /**
     * 将名字重新包装成数据库数据
     */
    public static String packing(String name) {
        return name.replaceAll(" ", "_");
    }

    /**
     * 更新任务
     */
    public static void updateTask(Task task) {
        taskDAO.update(task);
    }

    /**
     * 获得完整task名的任务名部分
     *
     * @param str 完整task名 格式为 分类名+任务名
     * @return 任务名
     */
    public static String getSubstringName(String str, char separator) {
        return str.substring(str.indexOf(separator) + 1);
    }

    /**
     * 获得完整task名的分类名部分
     *
     * @param str 完整task名 格式为 分类名+任务名
     * @return 分类名名
     */
    public static String getSubstringCategory(String str, char separator) {
        return str.substring(0, str.indexOf(separator));
    }

    public static Tag getTag(long tid) {
        return tagDAO.get(tid);
    }

    public static Category getCategory(String name) {
        return categoryDAO.get(name);
    }

    public static Tag getTag(String name) {
        return tagDAO.get(name);
    }

    /**
     * taskDAO为每一个分类维护一个最大编号
     *
     * @param tag
     * @return
     */
    public static int getMaxNOf(Tag tag) {
        return taskDAO.getMaxNOf(tag.getId());
    }
}
