package com.arsr.arsr;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import drawthink.expandablerecyclerview.bean.RecyclerViewData;

public class MainActivity extends BasicActivity {
    private DrawerLayout mDrawerLayout;//根布局
    //临时数据
    public String[] groupStrings = {"西游记", "水浒传", "三国演义", "红楼梦"};
    public String[][] childStrings = {
            {"唐三藏", "孙悟空", "猪八戒", "沙和尚"},
            {"宋江", "林冲", "李逵", "鲁智深"},
            {"曹操", "刘备", "孙权", "诸葛亮", "周瑜"},
            {"贾宝玉", "林黛玉", "薛宝钗", "王熙凤"}
    };
    private List<RecyclerViewData> mData;

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
        //任务列表（基于recyclerView）
        initmData();
        RecyclerView viewTaskList = findViewById(R.id.recycler_task_list);
        TaskListAdapter adapter = new TaskListAdapter(MainActivity.this, mData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        viewTaskList.setAdapter(adapter);
        viewTaskList.setLayoutManager(layoutManager);
    }

    /**
     * 封装初始化数据
     */
    private void initmData() {
        mData = new ArrayList<>();
        int groupLength = groupStrings.length;
        int childLength = childStrings.length;
        for (int i = 0; i < groupLength; i++) {
            List<String>childData = new ArrayList<>();
            for (int j = 0; j < childLength; j++) {
                childData.add(childStrings[i][j]);
            }
            mData.add(new RecyclerViewData(groupStrings[i], childData, true));
        }
    }

    /**
     * 加载右上角菜单
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
