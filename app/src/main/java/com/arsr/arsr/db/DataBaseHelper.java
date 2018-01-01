package com.arsr.arsr.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库初始化类
 * Created by KundaLin on 17/12/30.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {//开启外键支持
            db.execSQL("PRAGMA foreign_keys=1");
        }
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORY);
        db.execSQL(CREATE_TAG);
        db.execSQL(CREATE_TASK);
        db.execSQL(TRIGGER_CATEGORY);
        db.execSQL(TRIGGER_TAG);
        db.execSQL(TRIGGER_TASK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    private final String CREATE_CATEGORY = "create table category(\n" +
            "       id integer primary key autoincrement not null,\n" +
            "       name text not null unique,\n" +
            "       updateTime  text DEFAULT (strftime('%Y-%m-%d %H:%M:%S:%s','now', 'localtime'))\n" +
            ");";
    private final String CREATE_TAG = "create table tag(\n" +
            "       id integer primary key autoincrement,\n" +
            "       name text not null unique,\n" +
            "       prefix text not null,\n" +
            "       cid integer not null,\n" +
            "       note text,\n" +
            "       updateTime text DEFAULT(strftime('%Y-%m-%d %H:%M:%S:%s','now', 'localtime')),\n" +
            "       constraint fk_tag_cid_category foreign key(cid) references category(id)\n" +
            "       \n" +
            ");";
    private final String CREATE_TASK = "create table task(\n" +
            "       id integer primary key autoincrement,\n" +
            "       name text not null unique,\n" +
            "       times integer not null default 0,\n" +
            "       dayToRecall integer not null default 0,\n" +
            "       assistTimes integer not null default -1,\n" +
            "       dayToAssist integer not null default -1,\n" +
            "       tid integer not null,\n" +
            "       updateTime  text DEFAULT (strftime('%Y-%m-%d %H:%M:%S:%s','now', 'localtime')),\n" +
            "       constraint fk_task_tid_tag foreign key(tid) references tag(id)\n" +
            "       \n" +
            ");";
    private static final String TRIGGER_CATEGORY = "CREATE TRIGGER update_category_updatetime  BEFORE update ON category\n" +
            "begin\n" +
            "    update category set updateTime = strftime('%Y-%m-%d %H:%M:%S:%s','now', 'localtime') where id = old.id;\n" +
            "end;";
    private static final String TRIGGER_TAG = "CREATE TRIGGER update_tag_updatetime  BEFORE update ON tag\n" +
            "begin\n" +
            "    update tag set updateTime = strftime('%Y-%m-%d %H:%M:%S:%s','now', 'localtime') where id = old.id;\n" +
            "end;";
    private static final String TRIGGER_TASK = "CREATE TRIGGER update_task_updatetime  BEFORE update ON task\n" +
            "begin\n" +
            "    update task set updateTime = strftime('%Y-%m-%d %H:%M:%S:%s','now', 'localtime') where id = old.id;\n" +
            "end;";
}
