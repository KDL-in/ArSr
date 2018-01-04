package com.arsr.arsr.util;


import android.database.sqlite.SQLiteDatabase;

import com.arsr.arsr.MyApplication;

import com.arsr.arsr.db.Tag;
import com.arsr.arsr.db.Task;
import com.arsr.arsr.db.dao.CategoryDAO;
import com.arsr.arsr.db.dao.TagDAO;
import com.arsr.arsr.db.dao.TaskDAO;
import com.arsr.arsr.db.DataBaseHelper;

import java.io.File;

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
        return str.substring(str.indexOf(separator)+1);
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
}
