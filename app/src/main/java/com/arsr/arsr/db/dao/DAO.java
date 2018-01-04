package com.arsr.arsr.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.arsr.arsr.db.Entity;
import com.arsr.arsr.db.Tag;
import com.arsr.arsr.db.Task;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.LogUtil;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库对接对象，抽象出来的类
 * Created by KundaLin on 18/1/1.
 */

public abstract class DAO<T extends Entity> {
    protected List<T> list;//缓存列表
    protected Class<T> tClass;
    protected Map<String, Long> nameToId;//id映射
    protected Map<String, Integer> nameToIndex;//idx映射
    protected Map<Long, String> idToName;//名字映射
    protected Map<Long, Integer> idToIdx;//idx映射


    public DAO(Class<T> tClass) {
        this.tClass = tClass;
        nameToIndex = new HashMap<>();
        idToName=new HashMap<>();
        nameToId = new HashMap<>();
        idToIdx =new HashMap<>();
        loadCache();
    }

    /**
     * 删除
     * @param selection where条件
     * @param selectionArgs ？替换
     * @return id
     */
    public long delete(String selection, String[] selectionArgs) {
        return DBUtil.db.delete(tClass.getSimpleName(),selection, selectionArgs);
    }
    /**
     * 插入数据
     * @param values 插入值
     */
    public long insert(ContentValues values) {
        return DBUtil.db.insert(tClass.getSimpleName(), null, values);
    }
    public Cursor findAll()  {
        Cursor cursor = DBUtil.db.query(tClass.getSimpleName(), null, null, null, null, null, "updateTime DESC");
        return cursor;
    }

    /**
     * ===============================================================
     * 更新缓存
     * @param t 更新实体
     */
    public void updateCache(T t) {
        if (t.getId() == -1) return;//失败标志
        if (t.getName().equals(DBUtil.DELETED)) {//删除标志
            long id = t.getId();
            int idx = idToIdx.get(id);
            String name = idToName.get(id);
            list.set(idx, null);
            removeMap(name, id, idx);
        } else {//添加的情况
            list.add(t);
            buildMap(t.getName(),t.getId(),list.size()-1);
        }
    }
    /**
     * ===============================================================
     * 初始化缓存
     */
    public  void loadCache() {
        Cursor cursor = findAll();
        list = readCursor(cursor);
        for (int i = 0; i < list.size(); i++) {
            T e = list.get(i);
            buildMap(e.getName(),e.getId(),i);
        }
    }
    /**
     * 建立映射关系
     */
    public  void buildMap(String name, long id, int idx) {
        nameToId.put( name,id);
        idToName.put(id, name);
        nameToIndex.put(name, idx);
        idToIdx.put(id, idx);
    }

    /**、
     * 删除映射
     */
    private  void removeMap(String name, long id, int idx) {
        nameToId.remove(name);
        idToName.remove(id);
        nameToIndex.remove(name);
        idToIdx.remove(id);
    }
    /**
     * 取得name的id
     *
     * @param name 分类名
     * @return 它的id
     */
    public long getId(String name) {
        return nameToId.get(name);
    }

    /**
     * 从cursor中读取列表
     *
     * @param cursor
     * @return list<className>
     */
    public abstract List<T> readCursor(Cursor cursor);

    /**
     * 输出数据库中内容
     */
    public void display() {
        LogUtil.d("In "+tClass.getSimpleName());
        for (T e :
                list) {
            if (e != null)
                LogUtil.d(e.toString());
        }

    }

    public List<T> getList() {
        return list;
    }

    /**
     * 获取某个实体
     * @param name 实体名字
     * @return 实体对象
     */
    public T get(String name){
        return list.get(nameToIndex.get(name));
    }
    /**
     * 获取某个实体
     * @param id 实体id
     * @return 实体对象
     */
    public T get(long id) {
        return list.get(idToIdx.get(id));
    }

    /**
     * 更新
     * @param t 实体
     */
//    public abstract void update(T t);
}
