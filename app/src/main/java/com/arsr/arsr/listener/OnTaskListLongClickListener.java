package com.arsr.arsr.listener;

import android.content.Context;
import android.view.View;

import com.arsr.arsr.activity.TaskActivity;
import com.arsr.arsr.activity.TasksInCategoryActivity;
import com.arsr.arsr.adapter.TaskListRecyclerViewAdapter;
import com.arsr.arsr.db.Category;
import com.arsr.arsr.db.Task;

import drawthink.expandablerecyclerview.listener.OnRecyclerViewListener;

/**
 * taskList 长按打开独立界面
 * Created by KundaLin on 18/1/5.
 */

public class OnTaskListLongClickListener implements OnRecyclerViewListener.OnItemLongClickListener {
    TaskListRecyclerViewAdapter adapter;

    public OnTaskListLongClickListener(TaskListRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onGroupItemLongClick(int position, int groupPosition, View view) {
        Category category =adapter.getCategory(groupPosition);
        TasksInCategoryActivity.startAction(category.getName());
    }

    @Override
    public void onChildItemLongClick(int position, int groupPosition, int childPosition, View view) {
        Task task = adapter.getTask(groupPosition, childPosition);
        TaskActivity.actionStart(task.getName());
    }
}
