package com.arsr.arsr.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.arsr.arsr.db.Category;
import com.arsr.arsr.db.Entity;
import com.arsr.arsr.db.Tag;
import com.arsr.arsr.db.Task;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.IOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.arsr.arsr.util.DBUtil.taskDAO;

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
        if (id != -1) IOUtil.setPointInTimeOf(tag, "");
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

    /**
     * 删除名为name的实体
     *
     * @param name 实体名
     * @return 删除的相关子项的数量
     */

    public int delete(String name) {
        String selection = "name=?";
        String selectionArgs[] = {name};
        //处理任务表外键
        long id = getId(name);
        List<Task> relatedList = DBUtil.taskDAO.findRelatedItem(id);
        if (!relatedList.isEmpty()) {
            for (Task t :
                    relatedList) {
                DBUtil.taskDAO.delete(t.getId());
            }
        }
        delete(selection, selectionArgs);
        //更新缓存
        updateCache(id, DBUtil.DELETED);
        return relatedList.size();
    }

    //取出所有属于cName的tag
    public List<Tag> getListOf(String cName) {
        List<Tag> tags=new ArrayList<>();
        long cid = DBUtil.categoryDAO.getId(cName);
        for (Tag tag :
                list) {
            if (tag ==null) continue;
            if (tag.getCid() == cid) {
                tags.add(tag);
            }
        }
        return tags;
    }

    public String getName(long tid) {
        return idToName.get(tid);
    }
}

