package com.arsr.arsr.listener;

import android.view.View;

import com.arsr.arsr.activity.TaskActivity;
import com.arsr.arsr.activity.TasksInCategoryActivity;
import com.arsr.arsr.adapter.TasksInCategoryRecyclerViewAdapter;

import drawthink.expandablerecyclerview.listener.OnRecyclerViewListener;

/**
 * Created by KundaLin on 18/1/9.
 */

public class OnTicListClickListener implements OnRecyclerViewListener.OnItemClickListener {
    private TasksInCategoryActivity context;
    private TasksInCategoryRecyclerViewAdapter adapter;
    public OnTicListClickListener(TasksInCategoryActivity context, TasksInCategoryRecyclerViewAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public void onGroupItemClick(int position, int groupPosition, View view) {

    }

    @Override
    public void onChildItemClick(int position, int groupPosition, int childPosition, View view) {
        TaskActivity.actionStart(adapter.getChildData(groupPosition,position));
    }
}
