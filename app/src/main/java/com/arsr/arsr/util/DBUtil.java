package com.arsr.arsr.util;


import android.database.sqlite.SQLiteDatabase;

import com.arsr.arsr.MyApplication;

import db.DataBaseHelper;

/**
 * 数据库操作工具
 * 用于对接数据库，存取数据
 * Created by KundaLin on 17/12/29.
 */

public class DBUtil {

    public static String test1 = "insert into category(name)\n" +
            "values(\"CTE-6\");\n" +
            "insert into category(name)\n" +
            "values(\"Math\");\n" +
            "insert into category(name)\n" +
            "values(\"考研\");\n" +
            "\n" +
            "insert into tag(name,prefix,cid,note)\n" +
            "values(\"CTE-6_Word\",\"List\",1,null)";
    public static String test2 = "delete from category\n" +
            "where name=\"CTE-6\"";
    public static String test3 = "insert into tag(name,prefix,cid,note)\n" +
            "values(\"CTE-6_Word\",\"List\",1,null)";

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
}
