package com.arsr.arsr.util;

import android.content.Context;

import com.arsr.arsr.activity.TasksInCategoryActivity;
import com.arsr.arsr.adapter.CategoryListRecyclerViewAdapter;
import com.arsr.arsr.adapter.TaskListRecyclerViewAdapter;
import com.arsr.arsr.db.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import drawthink.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import drawthink.expandablerecyclerview.bean.RecyclerViewData;

/**
 * 将数据库数据生成可以给界面使用的列表适配器
 * Created by KundaLin on 17/12/31.
 */

public class ListUtil {
    private static Map<String, Integer> nameToInteger = new HashMap<>();

    /**
     * 获取分类-任务
     */
    public static TaskListRecyclerViewAdapter getTaskListAdapter(Context context) {
        List<String> group = getGroup(DBUtil.categoryDAO.getList());
        List<List<String>> children = getChildren(DBUtil.taskDAO.getList(), group);
        for (int i = 0; i < group.size(); i++) {
            if (children.get(i).isEmpty()) children.set(i, null);
        }
        TaskListRecyclerViewAdapter adapter = new TaskListRecyclerViewAdapter(context,getList(group, children, true));
        return adapter;
    }

    /**
     * 获取分类-标签列表
     */
    public static CategoryListRecyclerViewAdapter getTodayTasksAdapter(Context context) {
        List<String> group = getGroup(DBUtil.categoryDAO.getList());
        List<List<String>> children = getChildren(DBUtil.tagDAO.getList(), group);
        CategoryListRecyclerViewAdapter adapter = new CategoryListRecyclerViewAdapter(context,getList(group,children,false));
        return adapter;
    }

    private static List<RecyclerViewData> getList(List<String> group, List<List<String>> children, boolean isExpand) {
        List<RecyclerViewData> listData = new ArrayList<>();
        for (int i = 0; i < group.size(); i++) {
            if (children.get(i) != null)
                listData.add(new RecyclerViewData(group.get(i), children.get(i), isExpand));
        }
        return listData;
    }

    /**
     * 获取子项列表
     */
    private static <T extends Entity> List<List<String>> getChildren(List<T> list, List<String> group) {
        List<List<String>> children = new ArrayList<>();
        for (int i = 0; i < group.size(); i++) {
            List<String> childList = new ArrayList<>();
            children.add(childList);
        }
        for (T t :
                list) {
            if (t != null) {
                String nameStr[] = DBUtil.parse(t.getName());
                String name = "";
                for (int i = 0; i < nameStr.length; i++) {
                    if (i != nameStr.length - 1)
                        name += nameStr[i] + " ";
                    else
                        name += nameStr[i];
                }
                children.get(nameToInteger.get(nameStr[0])).add(name);
            }
        }
        return children;
    }

    /**
     * 获取组名列表
     */
    private static <T extends Entity> List<String> getGroup(List<T> list) {
        List<String> group = new ArrayList<>();
        nameToInteger.clear();
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            if (t != null) {
                group.add(t.getName());
                nameToInteger.put(t.getName(), group.size() - 1);
            }
        }
        return group;
    }

    public static TaskListRecyclerViewAdapter getTaskListAt(Context context, int day) {//todo
        //todo 获取某天的任务
        return getTaskListAdapter(context);
    }

    public static BaseRecyclerViewAdapter getTICListAdapter(Context context) {
        //todo 测试代码
        List<String> group = getGroup(DBUtil.categoryDAO.getList());
        List<List<String>> children = getChildren(DBUtil.tagDAO.getList(), group);
        CategoryListRecyclerViewAdapter adapter = new CategoryListRecyclerViewAdapter(context,getList(group,children,false));
        return adapter;
    }
}
