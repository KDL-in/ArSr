package com.arsr.arsr.dao;

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


/*
package com.arsr.arsr.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.arsr.arsr.util.DBUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arsr.arsr.db.Category;
import com.arsr.arsr.util.LogUtil;

*/
/**
 * Created by KundaLin on 17/12/31.
 * <p>
 * ====================================================================
 *
 * @param name
 * @return 插入id
 * <p>
 * 删除名为name的分类
 * @param name 分类名
 * <p>
 * ===============================================================
 * 更新缓存
 * @param category
 * <p>
 * 建立映射关系
 * 、
 * 删除映射
 * <p>
 * ===============================================================
 * 初始化缓存
 * <p>
 * 取得name的id
 * @param name 分类名
 * @return 它的id
 * <p>
 * ===============================================================
 * 从cursor中读取列表
 * @param cursor
 * @return list<className>
 * <p>
 * 输出数据库中内容
 *//*


public class CategoryDAO {


    */
/**
 * ====================================================================
 *
 * @param name
 * @return 插入id
 *//*

    public static long insert(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        long id = DBUtil.insert(Category.class, values);
        Category category = new Category(id, name);
        updateCache(category);
        return id;
    }

    */
/**
 * 删除名为name的分类
 *
 * @param name 分类名
 *//*

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

    */
/**
 * ===============================================================
 * 更新缓存
 *
 * @param category
 *//*

    private static void updateCache(Category category) {
        if (category.getId() == -1) return;//失败标志
        if (category.getName().equals(DBUtil.DELETED)) {//删除标志
            long id = category.getId();
            int idx = idToIdx.get(id);
            String name = idToName.get(id);
            list.set(idx, null);
            removeMap(name, id, idx);

        } else {//添加的情况
            list.add(category);
            buildMap(category.getName(),category.getId(),list.size());
        }
    }
    */
/**
 * 建立映射关系
 *//*

    public static void buildMap(String name, long id, int idx) {
        nameToId.put( name,id);
        idToName.put(id, name);
        nameToIndex.put(name, idx);
        idToIdx.put(id, idx);
    }

    */
/**、
 * 删除映射
 *//*

    private static void removeMap(String name, long id, int idx) {
        nameToId.remove(name);
        idToName.remove(id);
        nameToIndex.remove(name);
        idToIdx.remove(id);
    }

    */
/**
 * ===============================================================
 * 初始化缓存
 *//*

    public static List<Category> list;//缓存列表，提高效率
    private static Map<String, Long> nameToId;//id映射
    private static Map<String,Integer> nameToIndex;//下标映射
    private static Map<Long,String> idToName;//名字映射
    private static Map<Long,Integer> idToIdx;
    public static void loadCache() {
        Cursor cursor = DBUtil.findAll(Category.class);
        list = readCursor(cursor);
        nameToIndex = new HashMap<>();
        idToName=new HashMap<>();
        nameToId = new HashMap<>();
        idToIdx =new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            Category e = list.get(i);
            buildMap(e.getName(),e.getId(),i);
        }
    }



    */
/**
 * 取得name的id
 * @param name 分类名
 * @return 它的id
 *//*

    public static long getId(String name) {
        return nameToId.get(name);
    }

    */
/**
 * ===============================================================
 * 从cursor中读取列表
 *
 * @param cursor
 * @return list<className>
 *//*

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

    */
/**
 * 输出数据库中内容
 *//*

    public static void display() {
        LogUtil.d("In category");
        for (Category e :
                list) {
            if (e!=null)
            LogUtil.d(e.toString());
        }

    }


}
*/
