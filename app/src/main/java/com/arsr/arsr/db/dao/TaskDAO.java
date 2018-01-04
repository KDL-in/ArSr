package com.arsr.arsr.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.arsr.arsr.db.Entity;
import com.arsr.arsr.db.Task;
import com.arsr.arsr.util.DBUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *  Task表的数据库对接对象
 * Created by KundaLin on 17/12/31.
 */

public class TaskDAO extends DAO<Task>{


    public TaskDAO() {
        super(Task.class);
    }

    /**
     * ========================================================
     * 读取列表
     * @param cursor 查询结果
     * @return task list
     */
    public   List<Task> readCursor(Cursor cursor) {
        List<Task> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex("id")));
                task.setName(cursor.getString(cursor.getColumnIndex("name")));
                task.setTimes(cursor.getInt(cursor.getColumnIndex("times")));
                task.setDayToRecall(cursor.getInt(cursor.getColumnIndex("dayToRecall")));
                task.setAssistTimes(cursor.getInt(cursor.getColumnIndex("assistTimes")));
                task.setDayToAssist(cursor.getInt(cursor.getColumnIndex("dayToAssist")));
                task.setTid(cursor.getInt(cursor.getColumnIndex("tid")));
                list.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void update(Task task) {
        String sql = "update Task\n" +
                "set name = \"" + task.getName() + "\",times=" + task.getTimes() + ",dayToRecall=" + task.getDayToRecall() + ",assistTimes=" + task.getAssistTimes() + ",dayToAssist=" + task.getDayToAssist() + "\n" +
                "where id = "+task.getId();
        DBUtil.db.execSQL(sql);
    }

    /**
     * 插入任务
     *
     * @param taskName    任务名
     * @param times       次数
     * @param dayToRecall recall的剩余天数
     * @param assistTimes 辅助recall的次数
     * @param dayToAssist 辅助recall的剩余天数
     * @param tagName     标签名
     * @return 插入后的id，错误则返回-1
     */
    public  long insert(String taskName, int times, int dayToRecall, int assistTimes, int dayToAssist, String tagName) {
        ContentValues values = new ContentValues();
        String name = DBUtil.parse(tagName)[0] + "_" + taskName;
        long tid = DBUtil.tagDAO.getId(tagName);
        values.put("name", name);
        values.put("times", times);
        values.put("dayToRecall", dayToRecall);
        values.put("assistTimes", assistTimes);
        values.put("dayToAssist",dayToAssist);
        values.put("tid", tid);
        long id = insert(values);
        Task task = new Task(id, times, dayToRecall, assistTimes, dayToAssist, name, tid);
        updateCache(task);
        return id;
    }

    /**
     * 获得今日任务
     * @return 今日任务
     */
    public List<Task> getTodayList() {
        List<Task> todayTasks = new ArrayList<>();
        List<Task> tasks = getList();
        for (Task t :
                tasks) {
            if (t.getDayToRecall() == 0 || t.getDayToRecall() == -1 || t.getDayToAssist() == 0 || t.getDayToAssist() == -1) {
                todayTasks.add(t);
            }
        }
        return todayTasks;
    }
}/*public class TaskDAO {
    private static Map<Long, Task> taskMap;
    private static Map<String, Long> idMap;
    *//**
     * =====================================================
     * 初始化化缓存
     *//*
    public static void loadCache() {
        taskMap = new HashMap<>();
        idMap = new HashMap<>();
        Cursor cursor = DBUtil.findAll(Task.class);
        List<Task> tasks = readCursor(cursor);
        for (Task task :
                tasks) {
            updateCache(task);
        }
    }
    *//**
     * ==============================================
     * 更新缓存
     * @param task 更新实体
     *//*
    private static void updateCache(Task task) {
        if (task.getId()==-1)return;
        idMap.put(task.getName(), task.getId());
        taskMap.put(task.getId(), task);
    }
    public static void reloadCache() {
        loadCache();
    }
    *//**
     * ========================================================
     * 读取列表
     * @param cursor 查询结果
     * @return task list
     *//*
    private static List<Task> readCursor(Cursor cursor) {
        List<Task> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex("id")));
                task.setName(cursor.getString(cursor.getColumnIndex("name")));
                task.setTimes(cursor.getInt(cursor.getColumnIndex("times")));
                task.setDayToRecall(cursor.getInt(cursor.getColumnIndex("dayToRecall")));
                task.setAssistTimes(cursor.getInt(cursor.getColumnIndex("assistTimes")));
                task.setDayToAssist(cursor.getInt(cursor.getColumnIndex("dayToAssist")));
                task.setTid(cursor.getInt(cursor.getColumnIndex("tid")));
                list.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    *//**
     * 输出数据库中内容
     *//*
    public static void display() {
        LogUtil.d("In task");
        for (long key :
                taskMap.keySet()) {
            LogUtil.d(taskMap.get(key).toString());
        }
    }
    *//**
     * 插入任务
     *
     * @param taskName    任务名
     * @param times       次数
     * @param dayToRecall recall的剩余天数
     * @param assistTimes 辅助recall的次数
     * @param dayToAssist 辅助recall的剩余天数
     * @param tagName     标签名
     * @return 插入后的id，错误则返回-1
     *//*
    public static long insert(String taskName, int times, int dayToRecall, int assistTimes, int dayToAssist, String tagName) {
        ContentValues values = new ContentValues();
        String name = DBUtil.parse(tagName)[0] + "_" + taskName;
        long tid = DBUtil.tagDAO.getId(tagName);
        values.put("name", name);
        values.put("times", times);
        values.put("dayToRecall", dayToRecall);
        values.put("assistTimes", assistTimes);
        values.put("dayToAssist",dayToAssist);
        values.put("tid", tid);
        long id = DBUtil.insert(Task.class, values);
        Task task = new Task(id, times, dayToRecall, assistTimes, dayToAssist, name, tid);
        updateCache(task);
        return id;
    }
}*/
