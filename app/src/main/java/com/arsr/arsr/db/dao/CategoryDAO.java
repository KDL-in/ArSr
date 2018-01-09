package com.arsr.arsr.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.arsr.arsr.db.Category;
import com.arsr.arsr.db.Tag;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.LogUtil;

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
     * 删除更新缓存
     */
    @Override
    void updateCache(long id, String flag) {
        Category category = new Category();
        category.setId(id);
        category.setName(flag);
        updateCache(category);
    }

    public void delete(String name) {
        String selection = "name=?";
        String selectionArgs[] = {name};
        //处理tag表外键
        long id = getId(name);
        List<Tag> relatedList = DBUtil.tagDAO.findRelatedItem(id);
        for (Tag t :
                relatedList) {
            DBUtil.tagDAO.delete(t.getName());
        }
        delete(selection, selectionArgs);
        updateCache(id, DBUtil.DELETED);
    }
}

