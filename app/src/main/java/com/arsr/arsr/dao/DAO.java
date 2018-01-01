package com.arsr.arsr.dao;

import com.arsr.arsr.db.Category;
import com.arsr.arsr.util.LogUtil;

import java.util.List;

/**
 * Created by KundaLin on 18/1/1.
 */

public class DAO<T> {
    protected List<T> list;
    /**
     * 输出数据库中内容
     */
    public void display() {
        LogUtil.d("In ");
        for (T e :
                list) {
            if (e!=null)
                LogUtil.d(e.toString());
        }

    }
}
