package com.arsr.arsr.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.arsr.arsr.db.Category;
import com.arsr.arsr.db.Entity;
import com.arsr.arsr.db.Task;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Task表的数据库对接对象
 * Created by KundaLin on 17/12/31.
 */

public class TaskDAO extends DAO<Task> {
    private Map<Long, Integer> maxNs;

    public TaskDAO() {
        super(Task.class);
        maxNs = new HashMap<>();
        initMaxNs();
    }

    public void initMaxNs() {
        for (Task t :
                list) {
            updateMaxNOf(t.getTid(), t.getName());//更新缓存
        }
    }
    /**
     * ========================================================
     * 读取列表
     *
     * @param cursor 查询结果
     * @return task list
     */
    public List<Task> readCursor(Cursor cursor) {
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
        String sql = "update Task " +
                "set name = \"" + task.getName() + "\",times=" + task.getTimes() + ",dayToRecall=" + task.getDayToRecall() + ",assistTimes=" + task.getAssistTimes() + ",dayToAssist=" + task.getDayToAssist() + " " +
                "where id = " + task.getId();
        LogUtil.e("update",sql,"");
        DBUtil.db.execSQL(sql);
        updateCache(task);//更新缓存
        updateMaxNOf(task.getTid(), task.getName());//维护分类最大n
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
    public long insert(String taskName, int times, int dayToRecall, int assistTimes, int dayToAssist, String tagName) {
        ContentValues values = new ContentValues();//测试方法，不调用
        String name = DBUtil.parse(tagName)[0] + "_" + taskName;
        long tid = DBUtil.tagDAO.getId(tagName);
        values.put("name", name);
        values.put("times", times);
        values.put("dayToRecall", dayToRecall);
        values.put("assistTimes", assistTimes);
        values.put("dayToAssist", dayToAssist);
        values.put("tid", tid);
        long id = insert(values);
        Task task = new Task(id, times, dayToRecall, assistTimes, dayToAssist, name, tid);
        updateCache(task);
        updateMaxNOf(task.getTid(), task.getName());
        return id;
    }
    @Override
    public long insert(Task task) {
        ContentValues values = new ContentValues();
        values.put("name", task.getName());
        values.put("times", task.getTimes());
        values.put("dayToRecall", task.getDayToRecall());
        values.put("assistTimes", task.getAssistTimes());
        values.put("dayToAssist", task.getDayToAssist());
        values.put("tid", task.getTid());
        long id = insert(values);
        task.setId(id);
        updateCache(task);
        if (id!=-1)updateMaxNOf(task.getTid(), task.getName());
        return id;
    }
    public long insert(String taskName, String tagName) {
        //构造task
        Task task = new Task();
        task.setTid(DBUtil.tagDAO.getId(tagName));
        task.setName(taskName);
        return insert(task);
    }
    /**
     * 获得今日任务
     *
     * @return 今日任务
     */
    public List<Task> getTodayList() {
        List<Task> todayTasks = new ArrayList<>();
        List<Task> tasks = getList();
        for (Task t :
                tasks) {
            if (t==null) continue;
            if (t.getDayToRecall() == 0 || t.getDayToRecall() == -1 || t.getDayToAssist() == 0 || t.getDayToAssist() == -1) {
                todayTasks.add(t);
            }
        }
        return todayTasks;
    }

    /**
     * 获得某分类的最大编号
     *
     * @param tid 分类id
     * @return
     */
    public int getMaxNOf(long tid) {
        if (maxNs.containsKey(tid))
            return maxNs.get(tid);
        else
            return 0;
    }

    private void updateMaxNOf(long tid, String name) {
        int curN = Integer.parseInt(name.substring(name.lastIndexOf('_') + 1));
        if (!maxNs.containsKey(tid)) {
            maxNs.put(tid, curN);
            return;
        }
        int maxN = getMaxNOf(tid);
        if (curN > maxN) maxNs.put(tid, curN);
    }

    /**
     * 删除更新缓存
     */
    @Override
    void updateCache(long id, String flag) {
        Task task = new Task();
        task.setId(id);
        task.setName(flag);
        updateCache(task);
    }

    /**
     * 找到应用外键tid的task
     */
    public List<Task> findRelatedItem(long tid) {
        List<Task>relatedList = new ArrayList<>();
        for (Task t :
                list) {
            if (t==null)continue;
            if (t.getTid() == tid) {
                relatedList.add(t);
            }
        }
        return relatedList;
    }

    public void delete(String name) {
        delete(nameToId.get(name));
    }

    /**
     * 获得分类下的所有任务
     *
     * @param tagMap 分类包含标签
     */
    public List<Task> getListOf(Map<String, Integer> tagMap) {
        List<Task> rList = new ArrayList<>();
        for (Task t :
                list) {
            if (t ==null) continue;
            String tagName = DBUtil.tagDAO.getName(t.getTid());
            if (tagMap.containsKey(tagName)) {
                rList.add(t);
            }
        }
        return rList;
    }

    public int quaryTimes(String name) {
        if (nameToIndex.containsKey(name))
            return list.get(nameToIndex.get(name)).getTimes();
        return 0;
    }
}