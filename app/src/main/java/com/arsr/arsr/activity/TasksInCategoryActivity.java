package com.arsr.arsr.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.arsr.arsr.R;
import com.arsr.arsr.fragment.FragmentTaskList;
import com.arsr.arsr.util.ListUtil;

public class TasksInCategoryActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_in_category);
        //状态栏更换
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //滑动菜单
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //初始化列表
        FragmentTaskList fragment = FragmentTaskList.newInstance(ListUtil.getTICListAdapter(this));
        loadFragment(fragment);
    }

    private void loadFragment(FragmentTaskList fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout_ticUI_replace, fragment);
        transaction.commit();
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context,TasksInCategoryActivity.class);
        context.startActivity(intent);
    }
}