package com.arsr.arsr.activity;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.arsr.arsr.MyApplication;
import com.arsr.arsr.R;
import com.arsr.arsr.db.Category;
import com.arsr.arsr.fragment.FragmentTaskList;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.ListUtil;
import com.bumptech.glide.Glide;

public class TasksInCategoryActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_in_category);
        //获取数据
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        //状态栏更换
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //滑动菜单
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //设置task名
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle(category+"中任务进度");
        //设置图片
        ImageView imageView = findViewById(R.id.img_ticUI_bg);
        Glide.with(this).load(R.drawable.task_background).into(imageView);
        //初始化列表
        FragmentTaskList fragment = FragmentTaskList.newInstance(ListUtil.getTICListAdapter(this,category));
        loadFragment(fragment);
    }

    private void loadFragment(FragmentTaskList fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout_ticUI_replace, fragment);
        transaction.commit();
    }

    public static void startAction(String category) {
        Intent intent = new Intent(MyApplication.getContext(), TasksInCategoryActivity.class);
        intent.putExtra("category", category);
        MyApplication.getContext().startActivity(intent);
    }
}
