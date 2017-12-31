package com.arsr.arsr.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.arsr.arsr.util.DBUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.arsr.arsr.db.Category;
import com.arsr.arsr.util.LogUtil;

/**
 * Created by KundaLin on 17/12/31.
 */

public class CategoryDAO {
    private static Map<Long, Category> categoryMap;//缓存列表，提高效率
    private static Map<String, Long> idMap;//id映射

    /**
     * ====================================================================
     *
     * @param name
     * @return 插入id
     */
    public static long insert(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        long id = DBUtil.insert(Category.class, values);
        Category category = new Category(id, name);
        updateCache(category);
        return id;
    }

    /**
     * 删除名为name的分类
     *
     * @param name 分类名
     */
    public static void delete(String name) {
        String selection = "name=?";
        String selectionArgs[] = {name};
        delete(selection, selectionArgs);
        //更新缓存
        long id = getId(name);
        Category category = new Category(id, DBUtil.DELETED);
        updateCache(category);
    }

    private static long delete(String selection, String[] selectionArgs) {
        return DBUtil.delete(Category.class, selection, selectionArgs);
    }

    /**
     * ===============================================================
     * 更新缓存
     *
     * @param category
     */
    private static void updateCache(Category category) {
        if (category.getId() == -1) return;//失败标志
        if (category.getName().equals(DBUtil.DELETED)) {//删除标志
            Category rm = categoryMap.remove(category.getId());
            idMap.remove(rm.getName());
            TagDAO.reloadCache();
            TaskDAO.reloadCache();
            return;
        }
        idMap.put(category.getName(), category.getId());//插入
        categoryMap.put(category.getId(), category);
    }

    /**
     * ===============================================================
     * 初始化缓存
     */
    public static void loadCache() {
        Cursor cursor = DBUtil.findAll(Category.class);
        List<Category> categories = readCursor(cursor);
        categoryMap = new HashMap<>();
        idMap = new HashMap<>();
        for (Category category :
                categories) {
            updateCache(category);
        }
    }

    /**
     * 取得id为x的数据项
     */
    public Category getCategory(int id) {
        return categoryMap.get(id);
    }

    /**
     * 取得name的id
     *
     * @param name 分类名
     * @return 它的id
     */
    public static long getId(String name) {
        return idMap.get(name);
    }

    /**
     * ===============================================================
     * 从cursor中读取列表
     *
     * @param cursor
     * @return list<className>
     */
    private static List<Category> readCursor(Cursor cursor) {
        List<Category> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex("id")));
                category.setName(cursor.getString(cursor.getColumnIndex("name")));
                list.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * 输出数据库中内容
     */
    public static void display() {
        LogUtil.d("In category");
        for (long key :
                categoryMap.keySet()) {
            LogUtil.d(categoryMap.get(key).toString());
        }
    }


}
