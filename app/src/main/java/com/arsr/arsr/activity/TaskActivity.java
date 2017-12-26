package com.arsr.arsr.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.arsr.arsr.R;
import com.bumptech.glide.Glide;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;


/**
 * 任务界面活动
 */
public class TaskActivity extends BasicActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        //状态栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {//返回按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //设置task名
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("List 1");
        //设置图片
        ImageView imageView = findViewById(R.id.img_taskUI_bg);
        Glide.with(this).load(R.drawable.task_background).into(imageView);
        //日历相关
        MaterialCalendarView calendarView = findViewById(R.id.calendarView_month);

    }

    /**
     * 启动方法
     * @param context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, TaskActivity.class);
        context.startActivity(intent);
    }
}
