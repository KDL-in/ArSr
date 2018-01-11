package com.arsr.arsr.util;

import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import com.arsr.arsr.adapter.CategoryListRecyclerViewAdapter;
import com.arsr.arsr.adapter.NavigationRecyclerViewAdapter;
import com.arsr.arsr.adapter.TaskListRecyclerViewAdapter;
import com.arsr.arsr.adapter.TasksInCategoryRecyclerViewAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    public static final int TYPE_TASK_CHANGED = 1;
    public static final int TYPE_TAG_CHANGED = 2;
    public static final int TYPE_CATEGORY_CHANGE = 3;
    public static final String KEY_TASKS_CATEGORY = "tasks_in_caterory";
    private static Map<String, RecyclerView.Adapter> adapters;

    static {
        adapters = new HashMap<>();
    }

    /**
     * 按数据变动影响的类型更新数据
     */
    public static void updateUIData(int type) {
        if (type == TYPE_TASK_CHANGED) {
            updateList(KEY_TODAY_TASKS, KEY_TASKS_CATEGORY);
        } else if (type == TYPE_TAG_CHANGED) {
            updateList(KEY_CATEGORY_TAG_LIST, KEY_TODAY_TASKS, KEY_TASKS_CATEGORY);
        } else if (type == TYPE_CATEGORY_CHANGE) {
            updateList(KEY_CATEGORY, KEY_CATEGORY_TAG_LIST, KEY_TODAY_TASKS, KEY_TASKS_CATEGORY);
        }

    }

    /**
     * 更新数据
     */
    public static void updateList(String... keys) {
        for (String key :
                keys) {
            if (!adapters.containsKey(key)) continue;
            RecyclerView.Adapter adapter = adapters.get(key);
            switch (key) {
                case KEY_TODAY_TASKS:
                    ((TaskListRecyclerViewAdapter) adapter).setAllDatas(ListUtil.getTodayTaskList());
                    break;
                case KEY_CATEGORY_TAG_LIST:
                    ((CategoryListRecyclerViewAdapter) adapter).setAllDatas(ListUtil.getTagInCategoryList());
                    break;
                case KEY_CATEGORY:
                    ((NavigationRecyclerViewAdapter) adapter).setAllDatas(ListUtil.getCategoryList());
                    break;
                case KEY_TASKS_CATEGORY:
                    ((TasksInCategoryRecyclerViewAdapter) adapter).setAllDatas(ListUtil.getTicList(((TasksInCategoryRecyclerViewAdapter) adapter).getCategory()));
                    break;
            }
        }

    }

    /**
     * 添加数据列表，便于更新传递
     */
    public static void add(String type, RecyclerView.Adapter adapter) {
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

    /**
     * 禁止EditText输入空格
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.equals("_"))
                    return "";
                return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText) {

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 为edit设置约束
     *
     * @param edits
     */
    public static void setConstrain(EditText... edits) {
        for (EditText e :
                edits) {
            setEditTextInhibitInputSpace(e);
        }
    }

    public static void insertCategory(String name) {
        NavigationRecyclerViewAdapter adapter = (NavigationRecyclerViewAdapter) adapters.get(KEY_CATEGORY);
        adapter.add(name);
    }

    public static void remove(String key) {
        if (adapters.containsKey(key))
            adapters.remove(key);
    }
}
