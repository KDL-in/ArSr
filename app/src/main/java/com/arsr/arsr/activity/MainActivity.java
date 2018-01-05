package com.arsr.arsr.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import android.view.Window;
import android.view.WindowManager;

import com.arsr.arsr.R;
import com.arsr.arsr.adapter.NavigationRecyclerViewAdapter;
import com.arsr.arsr.adapter.MainFragmentPagerAdapter;
import com.arsr.arsr.adapter.TaskListRecyclerViewAdapter;
import com.arsr.arsr.db.dao.CategoryDAO;
import com.arsr.arsr.db.dao.TagDAO;
import com.arsr.arsr.db.dao.TaskDAO;
import com.arsr.arsr.fragment.FragmentTaskList;
import com.arsr.arsr.listener.OnMainPagerChangeListener;
import com.arsr.arsr.listener.OnTaskListClickListener;
import com.arsr.arsr.listener.OnTaskListLongClickListener;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.ListUtil;


public class MainActivity extends BasicActivity {
    private DrawerLayout mDrawerLayout;//根布局

    private MainFragmentPagerAdapter pagerAdapter;//碎片页面适配器

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

        //主界面两个子页面初始化
        pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), this);
        TaskListRecyclerViewAdapter listAdapter = ListUtil.getTodayTaskListAdapter(MainActivity.this);
        FragmentTaskList fragmentTaskList = FragmentTaskList.newInstance(listAdapter);
        FragmentTaskList fragmentCategoryList = FragmentTaskList.newInstance(ListUtil.getTagInCategoryAdapter(MainActivity.this));
        fragmentTaskList.setOnItemClickListener(new OnTaskListClickListener(listAdapter));
        fragmentTaskList.setOnItemLongClickListener(new OnTaskListLongClickListener(listAdapter));
        pagerAdapter.addFragment(fragmentTaskList);//添加碎片
        pagerAdapter.addFragment(fragmentCategoryList);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new OnMainPagerChangeListener(viewPager,MainActivity.this));//滑动监听
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);//自定义tab
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }//end自定义tab

        //navigationView上面的监听
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);
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
        RecyclerView recyclerView = findViewById(R.id.recyclerView_navigation);
        NavigationRecyclerViewAdapter adapter = new NavigationRecyclerViewAdapter(ListUtil.getCategoryAdapter());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    /**
     * 写一些测试用代码
     */
    private void test() {
        CategoryDAO categoryDAO =DBUtil.categoryDAO;
        TagDAO tagDAO = DBUtil.tagDAO;
        TaskDAO taskDAO = DBUtil.taskDAO;
        //category
        categoryDAO.insert("CTE-6");
        categoryDAO.insert("Math");
        categoryDAO.insert("kaoyan");
        categoryDAO.insert("计算机网络");
        //tag
        tagDAO.insert("Word","List","CTE-6","单词");
        tagDAO.insert("Reading 2016","Text","CTE-6","长难句");
        tagDAO.insert("Word","List","kaoyan",null);
        tagDAO.insert("Chapter","Chapter","Math",null);
        tagDAO.insert("Chapter", "C", "计算机网络", null);
        //task
        //values("CTE-6_List_1",4,0,-1,-1,1);
        taskDAO.insert("List_1",4,0,-2,-2,"CTE-6_Word");
        taskDAO.insert("List_2",3,0,-2,-2,"CTE-6_Word");
        taskDAO.insert("List_3",2,0,-2,-2,"CTE-6_Word");
        taskDAO.insert("List_4",1,0,-2,-2,"CTE-6_Word");
        taskDAO.insert("List_5",2,0,-2,-2,"CTE-6_Word");
        taskDAO.insert("C_1",3,0,-2,-2,"Math_Chapter");
        taskDAO.insert("C_2",3,0,-2,-2,"Math_Chapter");
        taskDAO.insert("List_1",4,0,-2,-2,"kaoyan_Word");
        taskDAO.insert("List_2",2,0,-2,-2,"kaoyan_Word");
        taskDAO.insert("List_3",2,0,-2,-2,"kaoyan_Word");
        categoryDAO.display();
        tagDAO.display();
        taskDAO.display();
        //更新
        categoryDAO.display();
        tagDAO.display();
        taskDAO.display();
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
