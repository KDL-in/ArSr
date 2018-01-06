package com.arsr.arsr.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.arsr.arsr.db.Category;
import com.arsr.arsr.util.DBUtil;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO extends DAO<Category> {

    public CategoryDAO() {
        super(Category.class);
    }

    @Override
    public List readCursor(Cursor cursor) {
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
     * 插入分类
     *
     * @param name 分类名
     * @return 插入后的id，-1代表错误
     */
    public long insert(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        long id = insert(values);
        Category category = new Category(id, name);
        updateCache(category);
        return id;
    }
    @Override
    public long insert(Category category) {
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        long id = insert(values);
        category.setId(id);
        updateCache(category);
        return id;
    }
    /**
     * 删除名为name的分类
     * @param name 分类名
     */

    public  void delete(String name) {
        String selection = "name=?";
        String selectionArgs[] = {name};
        delete(selection, selectionArgs);
        //更新缓存
        long id = getId(name);
        Category category = new Category(id, DBUtil.DELETED);
        updateCache(category);
    }
}

