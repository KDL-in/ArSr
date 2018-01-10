package com.arsr.arsr.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.arsr.arsr.R;
import com.arsr.arsr.UpdateNextDayTasksService;
import com.arsr.arsr.adapter.CategoryListRecyclerViewAdapter;
import com.arsr.arsr.adapter.NavigationRecyclerViewAdapter;
import com.arsr.arsr.adapter.MainFragmentPagerAdapter;
import com.arsr.arsr.adapter.TaskListRecyclerViewAdapter;
import com.arsr.arsr.db.dao.CategoryDAO;
import com.arsr.arsr.db.dao.TagDAO;
import com.arsr.arsr.db.dao.TaskDAO;
import com.arsr.arsr.fragment.FragmentTaskList;
import com.arsr.arsr.listener.OnCategoryListClickListener;
import com.arsr.arsr.listener.OnCategoryListLongClickListener;
import com.arsr.arsr.listener.OnFabClickListener;
import com.arsr.arsr.listener.OnMainPagerChangeListener;
import com.arsr.arsr.listener.OnNavAddButtonListener;
import com.arsr.arsr.listener.OnNavigationItemClickListener;
import com.arsr.arsr.listener.OnTaskListClickListener;
import com.arsr.arsr.listener.OnTaskListLongClickListener;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.DateUtil;
import com.arsr.arsr.util.ListUtil;
import com.arsr.arsr.util.LogUtil;

import java.util.Collections;


public class MainActivity extends BasicActivity {
    private DrawerLayout mDrawerLayout;//根布局

    private MainFragmentPagerAdapter pagerAdapter;//碎片页面适配器
    private NavigationView navigationView;

    private NavigationRecyclerViewAdapter navigationListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //状态栏更换
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //test
        test();
        //滑动菜单
        mDrawerLayout = findViewById(R.id.drawerLayout);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//利用返回按钮做导航按钮
            actionBar.setHomeAsUpIndicator(R.drawable.icon_menu);
        }
        //浮动按钮
        FloatingActionButton fab = findViewById(R.id.fab_mainUI);
        fab.setOnClickListener(new OnFabClickListener(MainActivity.this));
        //主界面两个子页面初始化
        pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), this);
        //-任务列表
        TaskListRecyclerViewAdapter taskAdapter = ListUtil.getTodayTaskListAdapter(MainActivity.this);
        FragmentTaskList fragmentTaskList = FragmentTaskList.newInstance(taskAdapter);//任务列表
        //-分类列表
        final CategoryListRecyclerViewAdapter categoryAdapter = ListUtil.getTagInCategoryAdapter(MainActivity.this);
        ;
        FragmentTaskList fragmentCategoryList = FragmentTaskList.newInstance(categoryAdapter);
        //-设置监听
        fragmentTaskList.setOnItemClickListener(new OnTaskListClickListener(taskAdapter));//tasklist监听
        fragmentTaskList.setOnItemLongClickListener(new OnTaskListLongClickListener(taskAdapter));
        fragmentCategoryList.setOnItemClickListener(new OnCategoryListClickListener(MainActivity.this, categoryAdapter));
        fragmentCategoryList.setOnItemLongClickListener(new OnCategoryListLongClickListener(MainActivity.this, categoryAdapter));
        pagerAdapter.addFragment(fragmentTaskList);//添加碎片
        pagerAdapter.addFragment(fragmentCategoryList);
        ViewPager viewPager = findViewById(R.id.viewPager);//设置adapter
        viewPager.addOnPageChangeListener(new OnMainPagerChangeListener(viewPager, MainActivity.this));//滑动监听
        viewPager.setAdapter(pagerAdapter);
        //-自定义tab
        final TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

        //navigationView
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setCheckedItem(R.id.nav_today);
        //-中间菜单
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {//todo 失焦问题
                    case R.id.nav_today:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_calendar:
                        CalendarTaskActivity.startAction(MainActivity.this);
//                        navigationView.getMenu().performIdentifierAction(R.id.nav_today, 0);
                        drawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });
        //-navigationView上的列表
        RecyclerView recyclerView = findViewById(R.id.recyclerView_navigation);
        navigationListAdapter = ListUtil.getCategoryAdapter();
        navigationListAdapter.setOnItemClickListener(new OnNavigationItemClickListener(MainActivity.this, navigationListAdapter));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(navigationListAdapter);
        //-底部添加按钮
        final View view = findViewById(R.id.nav_addCategory);
        view.setOnClickListener(new OnNavAddButtonListener(MainActivity.this));
        //-上下滑动
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    //分别把中间所有的item的位置重新交换
                    for (int i = fromPosition; i < toPosition; i++) {
                        navigationListAdapter.swap(i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        navigationListAdapter.swap( i, i - 1);
                    }
                }
                navigationListAdapter.notifyItemMoved(fromPosition, toPosition);
                //返回true表示执行拖动
                return true;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        //定时任务
        DateUtil.setRepeatingTask(this,17,36,UpdateNextDayTasksService.class);
    }

    /**
     * 写一些测试用代码
     */
    private void test() {
        CategoryDAO categoryDAO = DBUtil.categoryDAO;
        TagDAO tagDAO = DBUtil.tagDAO;
        TaskDAO taskDAO = DBUtil.taskDAO;
        //category
        categoryDAO.insert("CTE-6");
        categoryDAO.insert("Math");
        categoryDAO.insert("kaoyan");
        categoryDAO.insert("计算机网络");
        //tag
        tagDAO.insert("Word", "List", "CTE-6", "单词");
        tagDAO.insert("Reading2016", "Text", "CTE-6", "长难句");
        tagDAO.insert("Word", "List", "kaoyan", null);
        tagDAO.insert("Chapter", "Chapter", "Math", null);
        tagDAO.insert("Chapter", "C", "计算机网络", null);
        //task
        //values("CTE-6_List_1",4,0,-1,-1,1);
        taskDAO.insert("List_1", 5, 0, -2, -2, "CTE-6_Word");
        taskDAO.insert("List_2", 3, 0, -2, -2, "CTE-6_Word");
        taskDAO.insert("List_3", 2, 0, -2, -2, "CTE-6_Word");
        taskDAO.insert("List_4", 1, 0, -2, -2, "CTE-6_Word");
        taskDAO.insert("List_5", 2, 0, -2, -2, "CTE-6_Word");
        taskDAO.insert("C_1", 3, 0, -2, -2, "Math_Chapter");
        taskDAO.insert("C_2", 3, 0, -2, -2, "Math_Chapter");
        taskDAO.insert("List_1", 4, 0, -2, -2, "kaoyan_Word");
        taskDAO.insert("List_2", 2, 0, -2, -2, "kaoyan_Word");
        taskDAO.insert("List_3", 2, 0, -2, -2, "kaoyan_Word");
        taskDAO.insert("List_4", 4, 10, 1, 0, "kaoyan_Word");
//        categoryDAO.display();
//        tagDAO.display();
//        taskDAO.display();
        //更新
//        categoryDAO.display();
//        tagDAO.display();
//        taskDAO.display();
        //删除
//        categoryDAO.delete("CESHI");
//        categoryDAO.display();
//        categoryDAO.display();
//        IOUtil.test();
    }


    /**
     * 加载右上角菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * 右上角菜单点击事件
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
