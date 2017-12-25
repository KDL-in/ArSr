package com.arsr.arsr.activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arsr.arsr.CalendarTaskActivity;
import com.arsr.arsr.R;
import com.arsr.arsr.adapter.CategoryListRecyclerViewAdapter;
import com.arsr.arsr.adapter.MainFragmentPagerAdapter;
import com.arsr.arsr.fragment.CreateTaskFragment;
import com.arsr.arsr.fragment.TaskListFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends BasicActivity {
    private DrawerLayout mDrawerLayout;//根布局
    //临时数据
    public String[] groupStrings = {"CET-6", "考研", "Math", "PcNet"};
    public String[][] childStrings = {
            {"List 1", "List 2", "List 3", "List 3"},
            {"L 1", "L 2", "L 3", "L 4"},
            {"Chapter 1", "Chapter 2", "Chapter 3", "Chapter 4", "Chapter 5"},
            {"c1", "c2", "c3", "c4"}
    };

    private MainFragmentPagerAdapter pagerAdapter;//碎片页面适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //状态栏更换
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //滑动菜单
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//利用返回按钮做导航按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
/*
        //expandableListView任务列表
        ExpandableListView listView = findViewById(R.id.list_task_list);
        listView.setAdapter(new TaskListAdapterBak(this,groupStrings,childStrings));
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Toast.makeText(getApplicationContext(), groupStrings[groupPosition], Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), childStrings[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
*/

        //主界面两个子页面初始化
        pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(),this);
        initPaperAdapter();
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {//自定义tab
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

        //navigationView上面的监听
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_today);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_today:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_calendar:
                        CalendarTaskActivity.startAction(MainActivity.this);
                        break;
                }

                return true;
            }
        });
        //navigationView上的列表
        RecyclerView recyclerView = findViewById(R.id.recycler_category_list);
        CategoryListRecyclerViewAdapter adapter = new CategoryListRecyclerViewAdapter(Arrays.asList(groupStrings));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initPaperAdapter() {
        pagerAdapter.addFragment(TaskListFragment.newInstance(groupStrings, childStrings));
        pagerAdapter.addFragment(CreateTaskFragment.newInstance("",""));
    }



    /**
     * 加载右上角菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    /**
     * 右上角菜单点击事件
     * 1. todo 后面再对各种项监听
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.settings:
                break;
            default:
                break;
        }
        return true;
    }
}
