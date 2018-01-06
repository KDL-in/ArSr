package com.arsr.arsr.util;

import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.arsr.arsr.adapter.TaskListRecyclerViewAdapter;
import com.arsr.arsr.adapter.holder.VHTaskListRecyclerView;

import java.util.HashMap;
import java.util.Map;

import drawthink.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;

/**
 * 界面数据操作工具
 * 判断界面数据数据的合法性以及更新数据
 * Created by KundaLin on 18/1/6.
 */

public class UIDataUtil {
    public static final String TODAY_TASKS="today_tasks";
    private static Map<String, RecyclerView.Adapter> adapters;

    static {
        adapters = new HashMap<>();
    }

    /**
     * 检查editText是否为空
     */
    public static boolean isEmpty(EditText edit) {
        return edit.getText().toString().trim().equals("");
    }

    /**
     * 更新数据
     */
    public static void updateUI(String type, String taskName) {
        switch (type) {
            case TODAY_TASKS:
                TaskListRecyclerViewAdapter adapter = (TaskListRecyclerViewAdapter) adapters.get(type);
                adapter.update(taskName.replaceAll("_", " "));
                adapter.notifyRecyclerViewData();
                break;
        }
    }

    /**
     * 添加数据列表，便于更新传递
     */
    public static void add(String type, BaseRecyclerViewAdapter adapter) {
        adapters.put(type, adapter);
    }
}
