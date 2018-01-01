package com.arsr.arsr.util;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.arsr.arsr.MyApplication;

import com.arsr.arsr.dao.CategoryDAO;
import com.arsr.arsr.dao.TagDAO;
import com.arsr.arsr.dao.TaskDAO;
import com.arsr.arsr.db.DataBaseHelper;

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
    private static SQLiteDatabase db;
    static {
        helper = new DataBaseHelper(MyApplication.getContext(), "ArSrDB.db", null, 1);
        db = helper.getWritableDatabase();
    }

    /**
     * 获取数据库操作对象
     */
    public static SQLiteDatabase get() {
        return db;
    }

    public static <T> Cursor findAll(Class<T> tClass)  {
        Cursor cursor = db.query(tClass.getSimpleName(), null, null, null, null, null, "updateTime DESC");
        return cursor;
    }

    /**
     * 插入数据
     *
     * @param tClass 插入表对应类
     * @param values 插入值
     * @param <T>
     */
    public static <T> long insert(Class<T> tClass, ContentValues values) {
        return db.insert(tClass.getSimpleName(), null, values);
    }

    /**
     * 初始化读取数据库中表格作为缓存
     */
    public static void initCache() {
        CategoryDAO.loadCache();
        TagDAO.loadCache();
        TaskDAO.loadCache();
    }

    public static<T> long delete(Class<T> tClass, String selection, String[] selectionArgs) {
       return db.delete(tClass.getSimpleName(), selection, selectionArgs);
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
}
