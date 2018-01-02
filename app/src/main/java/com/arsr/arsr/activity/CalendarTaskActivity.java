package com.arsr.arsr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.arsr.arsr.R;
import com.arsr.arsr.adapter.TaskListRecyclerViewAdapter;
import com.arsr.arsr.fragment.FragmentTaskList;
import com.arsr.arsr.util.ListUtil;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.List;

import drawthink.expandablerecyclerview.bean.RecyclerViewData;

/**
 * 按日历查看任务列表
 */
public class CalendarTaskActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendarlist);
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
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitleEnabled(false);
        //日历相关
        MaterialCalendarView calendarView = findViewById(R.id.calendarView_week);

    }

    /**
     * 显示某一天的列表
     * todo 某天的查询
     */
    private void showListAt(int day) {

        TaskListRecyclerViewAdapter adapterTaskListRecyclerView = ListUtil.getTaskListAt(CalendarTaskActivity.this, day);
        FragmentTaskList fragment = FragmentTaskList.newInstance(ListUtil.getTaskListAt(this, day));
        loadFragment(fragment);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout_tadUI__replace, fragment);
        transaction.commit();
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, CalendarTaskActivity.class);
        context.startActivity(intent);
    }
}
