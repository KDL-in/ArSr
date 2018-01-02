package com.arsr.arsr.util;


import android.database.sqlite.SQLiteDatabase;

import com.arsr.arsr.MyApplication;

import com.arsr.arsr.db.dao.CategoryDAO;
import com.arsr.arsr.db.dao.TagDAO;
import com.arsr.arsr.db.dao.TaskDAO;
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
}
