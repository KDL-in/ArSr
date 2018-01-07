package com.arsr.arsr.util;

import android.support.v7.widget.RecyclerView;

import com.arsr.arsr.adapter.CategoryListRecyclerViewAdapter;
import com.arsr.arsr.adapter.TaskListRecyclerViewAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import drawthink.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;

/**
 * 界面数据操作工具
 * 判断界面数据数据的合法性以及更新数据
 * Created by KundaLin on 18/1/6.
 */

public class UIDataUtil {
    public static final int EXPANDABLE_LIST = 0;
    public static final String KEY_TODAY_TASKS = "today_task_list";
    public static final String KEY_CATEGORY_TAG_LIST = "category_tag_list";
    public static final String KEY_CATEGORY = "category_list";
    private static Map<String, RecyclerView.Adapter> adapters;

    static {
        adapters = new HashMap<>();
    }


    /**
     * 更新数据
     */
    public static void updateUI(String type) {
        if (type.equals(KEY_CATEGORY)) {
            //任务列表
        } else {
            BaseRecyclerViewAdapter adapter = (BaseRecyclerViewAdapter) adapters.get(type);
            switch (type) {
                case KEY_TODAY_TASKS:
                    ((TaskListRecyclerViewAdapter)adapter).setAllDatas(ListUtil.getTodayTaskList());
                    break;
                case KEY_CATEGORY_TAG_LIST:
                    adapter.setAllDatas(ListUtil.getTagInCategoryList());
                    break;
            }
        }
    }

    /**
     * 添加数据列表，便于更新传递
     */
    public static void add(String type, BaseRecyclerViewAdapter adapter) {
        adapters.put(type, adapter);
    }

    /**
     * 检测字符串是否为空
     */
    public static boolean checkEmpty(String... arr) {
        for (String s :
                arr) {
            if (s.equals("")) return true;
        }
        return false;
    }
}
