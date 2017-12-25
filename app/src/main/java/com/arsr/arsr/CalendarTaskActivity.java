package com.arsr.arsr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.arsr.arsr.activity.BasicActivity;
import com.arsr.arsr.adapter.TaskListRecycleViewAdapter;
import com.arsr.arsr.fragment.TaskListFragment;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.List;

import drawthink.expandablerecyclerview.bean.RecyclerViewData;

/**
 * 按日历查看任务列表
 */
public class CalendarTaskActivity extends BasicActivity {
    //测试数据
    public String[] groupStrings = {"CET-6", "考研", "Math", "PcNet"};
    public String[][] childStrings = {
            {"List 1", "List 2", "List 3", "List 3"},
            {"L 1", "L 2", "L 3", "L 4"},
            {"Chapter 1", "Chapter 2", "Chapter 3", "Chapter 4", "Chapter 5"},
            {"c1", "c2", "c3", "c4"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_task);
        //显示列表
        showListAt(0);
        //状态栏更换
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {//返回按钮
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
        //标题取消
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        //日历相关
        MaterialCalendarView calendarView = findViewById(R.id.calendar_week);

    }

    /**
     * 显示某一天的列表
     */
    private void showListAt(int day) {
        List<RecyclerViewData> taskListData = TaskListFragment.initExListData(groupStrings, childStrings);
        TaskListRecycleViewAdapter taskListRecycleViewAdapter = new TaskListRecycleViewAdapter(this, taskListData);
        RecyclerView recyclerView = findViewById(R.id.recycler_day_at);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(taskListRecycleViewAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, CalendarTaskActivity.class);
        context.startActivity(intent);
    }
}
