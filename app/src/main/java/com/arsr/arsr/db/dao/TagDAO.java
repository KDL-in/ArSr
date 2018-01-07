package com.arsr.arsr.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.arsr.arsr.db.Category;
import com.arsr.arsr.db.Tag;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.IOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * tag表操作类
 * Created by KundaLin on 17/12/31.
 */

public class TagDAO extends DAO<Tag> {
    private static Map<Long, Tag> tagMap;
    private static Map<String, Long> idMap;

    public TagDAO() {
        super(Tag.class);
    }


    /**
     * 读取列表
     *
     * @param cursor 查询结果
     * @return tag list
     */
    public List<Tag> readCursor(Cursor cursor) {
        List<Tag> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Tag tag = new Tag();
                tag.setId(cursor.getInt(cursor.getColumnIndex("id")));
                tag.setName(cursor.getString(cursor.getColumnIndex("name")));
                tag.setPrefix(cursor.getString(cursor.getColumnIndex("prefix")));
                tag.setCid(cursor.getInt(cursor.getColumnIndex("cid")));
                tag.setNote(cursor.getString(cursor.getColumnIndex("note")));
                list.add(tag);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


    /**
     * 插入tag
     *
     * @param tName  tag name
     * @param prefix prefix of tag
     * @param cName  category name
     * @param note   tag note
     * @return insert id
     */
    public long insert(String tName, String prefix, String cName, String note) {
        ContentValues values = new ContentValues();
        String name = cName + "_" + tName;
        long cid = DBUtil.categoryDAO.getId(cName);
        Tag tag = new Tag(0, name, prefix, cid, note);
        long id = insert(tag);
        return id;
    }

    @Override
    public long insert(Tag tag) {
        ContentValues values = new ContentValues();
        values.put("name", tag.getName());
        values.put("prefix", tag.getPrefix());
        values.put("cid", tag.getCid());
        values.put("note", tag.getNote());
        long id = insert(values);
        tag.setId(id);
        updateCache(tag);//更新缓存
        //设置默认recall时间点
        if (id!=-1)IOUtil.setPointInTimeOf(tag, "");
        return id;
    }
    /**
     * 删除更新缓存
     */
    @Override
    void updateCache(long id, String flag) {
        Tag tag = new Tag();
        tag.setId(id);
        tag.setName(flag);
        updateCache(tag);
    }


}
/*
public class TagDAO {
    private static Map<Long, Tag> tagMap;
    private static Map<String, Long> idMap;

    */
/**
 * =====================================================
 * 初始化化缓存
 * <p>
 * 取得id为x的数据项
 * <p>
 * 取得name的id
 *
 * @param name 分类名
 * @return 它的id
 * <p>
 * ========================================================
 * 读取列表
 * @param cursor 查询结果
 * @return tag list
 * <p>
 * 输出数据库中内容
 * <p>
 * ==================================================================
 * 插入tag
 * @param tName  tag name
 * @param prefix prefix of tag
 * @param cName  category name
 * @param note   tag note
 * @return insert id
 * <p>
 * ===============================================================
 * 更新缓存
 * @param tag 更新的标签
 *//*

    public static void loadCache() {
        tagMap = new HashMap<>();
        idMap = new HashMap<>();
        Cursor cursor = DBUtil.findAll(Tag.class);
        List<Tag> tags = readCursor(cursor);
        for (Tag tag :
                tags) {
            updateCache(tag);
        }
    }

    */
/**
 * 取得id为x的数据项
 *//*

    public Tag getCategory(int id) {
        return tagMap.get(id);
    }

    */
/**
 * 取得name的id
 *
 * @param name 分类名
 * @return 它的id
 *//*

    public static long getId(String name) {
        return idMap.get(name);
    }


    */
/**
 * ========================================================
 * 读取列表
 *
 * @param cursor 查询结果
 * @return tag list
 *//*

    private static List<Tag> readCursor(Cursor cursor) {
        List<Tag> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Tag tag = new Tag();
                tag.setId(cursor.getInt(cursor.getColumnIndex("id")));
                tag.setName(cursor.getString(cursor.getColumnIndex("name")));
                tag.setPrefix(cursor.getString(cursor.getColumnIndex("prefix")));
                tag.setCid(cursor.getInt(cursor.getColumnIndex("cid")));
                tag.setNote(cursor.getString(cursor.getColumnIndex("note")));
                list.add(tag);
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
        LogUtil.d("In tag");
        for (long key :
                tagMap.keySet()) {
            LogUtil.d(tagMap.get(key).toString());
        }
    }

    */
/**
 * ==================================================================
 * 插入tag
 *
 * @param tName  tag name
 * @param prefix prefix of tag
 * @param cName  category name
 * @param note   tag note
 * @return insert id
 *//*

    public static long insert(String tName, String prefix, String cName, String note) {
        ContentValues values = new ContentValues();
        String name = cName + "_" + tName;
        long cid = DBUtil.categoryDAO.getId(cName);
        values.put("name", name);
        values.put("prefix", prefix);
        values.put("cid", cid);
        values.put("note", note);
        long id = DBUtil.insert(Tag.class, values);
        Tag tag =new Tag(id, name, prefix,cid,note);
        updateCache(tag);
        return id;
    }
    */
/**
 * ===============================================================
 * 更新缓存
 * @param tag 更新的标签
 *//*

    private static void updateCache(Tag tag) {
        if (tag.getId() == -1) return;//失败标志
        if (tag.getName().equals(DBUtil.DELETED)) {//删除标志
            Tag rm = tagMap.remove(tag.getId());
            idMap.remove(rm.getName());
            TaskDAO.reloadCache();
            return;
        }
        idMap.put(tag.getName(), tag.getId());//插入
        tagMap.put(tag.getId(), tag);
    }

    public static void reloadCache() {
        loadCache();
    }
}
*/
