package com.arsr.arsr.listener;

import android.view.View;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.activity.MainActivity;
import com.arsr.arsr.activity.TasksInCategoryActivity;
import com.arsr.arsr.adapter.NavigationRecyclerViewAdapter;

/**
 * navigation上item的点击事件
 * Created by KundaLin on 18/1/7.
 */

public class OnNavigationItemClickListener implements NavigationRecyclerViewAdapter.OnClickListener {
    private NavigationRecyclerViewAdapter adapter;
    private MainActivity context;
    public OnNavigationItemClickListener(MainActivity context, NavigationRecyclerViewAdapter adapter) {
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    public void onItemClickListener(View v) {
        TextView textView = v.findViewById(R.id.tv_nav_categoryName);
        TasksInCategoryActivity.startAction(textView.getText().toString());
    }

    @Override
    public void onItemLongClickListener(View v) {

    }
}
