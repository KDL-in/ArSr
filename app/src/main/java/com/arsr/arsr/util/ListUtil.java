package com.arsr.arsr.util;

import android.content.Context;

import com.arsr.arsr.adapter.CategoryListRecyclerViewAdapter;
import com.arsr.arsr.adapter.NavigationRecyclerViewAdapter;
import com.arsr.arsr.adapter.TaskListRecyclerViewAdapter;
import com.arsr.arsr.adapter.TasksInCategoryRecyclerViewAdapter;
import com.arsr.arsr.db.Category;
import com.arsr.arsr.db.Entity;
import com.arsr.arsr.db.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import drawthink.expandablerecyclerview.bean.RecyclerViewData;

/**
 * 将数据库数据生成可以给界面使用的列表适配器
 * Created by KundaLin on 17/12/31.
 */

public class ListUtil {
    private static Map<String, Integer> nameToInteger = new HashMap<>();

    public static List<RecyclerViewData> getTodayTaskList() {
        List<String> group = getCategoryNameGroup();
        List<List<String>> children = getChildren(DBUtil.taskDAO.getTodayList(), group);
        for (int i = 0; i < group.size(); i++) {//去掉没子项的
            if (children.get(i).isEmpty()) children.set(i, null);
        }
        return getList(group, children, true);
    }
    /**
     * 获取分类-任务adapter
     */
    public static TaskListRecyclerViewAdapter getTodayTaskListAdapter(Context context) {
        TaskListRecyclerViewAdapter adapter = new TaskListRecyclerViewAdapter(context, getTodayTaskList());
        UIDataUtil.add(UIDataUtil.KEY_TODAY_TASKS, adapter);//添加给更新控制
        return adapter;
    }

    /**
     * 得到categories的名字列表
     */
    public static List<String> getCategoryNameGroup() {
        return getGroup(DBUtil.categoryDAO.getList());
    }
    /**
     * 得到tags的名字列表
     */
    public static List<List<String>> getTagNameChildren(List<String> group) {
        return getChildren(DBUtil.tagDAO.getList(), group);
    }
    /**
     * 获取分类-标签列表
     */
    public static List<RecyclerViewData> getTagInCategoryList() {
        List<String> group = getCategoryNameGroup();
        List<List<String>> children = getTagNameChildren(group);
        return getList(group, children, true);
    }
    /**
     * 获取分类-标签adapter
     */
    public static CategoryListRecyclerViewAdapter getTagInCategoryAdapter(Context context) {
        CategoryListRecyclerViewAdapter adapter = new CategoryListRecyclerViewAdapter(context, getTagInCategoryList());
        UIDataUtil.add(UIDataUtil.KEY_CATEGORY_TAG_LIST,adapter);//添加给更新控制
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
        for (int i = 0; i < group.size(); i++) {//初始化个组的children的list
            List<String> childList = new ArrayList<>();
            children.add(childList);
        }
        for (T t :
                list) {
            if (t != null) {
                String nameStr[] = DBUtil.parse(t.getName());//去掉下划线
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
            if (t != null) {//缓存中可能被删除
                group.add(t.getName());
                nameToInteger.put(t.getName(), group.size() - 1);
            }
        }
        return group;
    }



    /**
     * 任务分类管理列表
     */
    public static TasksInCategoryRecyclerViewAdapter getTICListAdapter(Context context, String categoryName) {
        List<RecyclerViewData> ticList = getTicList(categoryName);
        TasksInCategoryRecyclerViewAdapter adapter = new TasksInCategoryRecyclerViewAdapter(context, ticList,categoryName);
        UIDataUtil.add(UIDataUtil.KEY_TASKS_CATEGORY,adapter);
        return adapter;
    }

    public static List<RecyclerViewData> getTicList(String categoryName) {
        //group
        List<String> group = getTagNameGroup(categoryName);
        //child
        List<List<String>> children = new ArrayList<>();
        for (int i = 0; i < group.size(); i++) {//初始化个组的children的list
            List<String> childList = new ArrayList<>();
            children.add(childList);
        }
        List<Task> list = DBUtil.taskDAO.getListOf(nameToInteger);
        for (Task t :
                list) {
            String tagName = DBUtil.tagDAO.getName(t.getTid());
            children.get(nameToInteger.get(tagName)).add(t.getName());
        }
        return getList(group, children,true);
    }

    /**
     * 获取cName分类下所有标签的名字
     *
     * @param cName 分类名
     */
    private static List<String> getTagNameGroup(String cName) {
        return getGroup(DBUtil.tagDAO.getListOf(cName));
    }

    /**
     * 分类列表
     * @return 分类名列表
     */
    public static List<String> getCategoryList() {
        List<Category> list = DBUtil.categoryDAO.getList();
        return getGroup(list);
    }

    public static NavigationRecyclerViewAdapter getCategoryAdapter() {
        NavigationRecyclerViewAdapter adapter = new NavigationRecyclerViewAdapter(ListUtil.getCategoryList());
        UIDataUtil.add(UIDataUtil.KEY_CATEGORY,adapter);
        return adapter;
    }

    /**
     * 从所给的task数据构造adapter，分类-任务列表
     */
    public static TaskListRecyclerViewAdapter getListAdapterWith(Context context,List<Task> tasks) {
        TaskListRecyclerViewAdapter adapter = new TaskListRecyclerViewAdapter(context, getTaskListWith(tasks));
        return adapter;
    }

    /**
     * 从所给task数据构造列表
     */
    private static List<RecyclerViewData> getTaskListWith(List<Task> tasks) {
        if (tasks==null)tasks=new ArrayList<>();
        List<String> group = getCategoryNameGroup();
        List<List<String>> children = getChildren(tasks, group);
        for (int i = 0; i < group.size(); i++) {//去掉没子项的
            if (children.get(i).isEmpty()) children.set(i, null);
        }
        return getList(group,children,true);
    }
}
