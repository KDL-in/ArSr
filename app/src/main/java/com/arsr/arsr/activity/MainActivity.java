package com.arsr.arsr.activity;

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

import com.arsr.arsr.R;
import com.arsr.arsr.adapter.AdapterNavigationRecyclerView;
import com.arsr.arsr.adapter.AdapterMainFragmentPager;
import com.arsr.arsr.dao.CategoryDAO;
import com.arsr.arsr.dao.TagDAO;
import com.arsr.arsr.dao.TaskDAO;
import com.arsr.arsr.fragment.FragmentCategoryList;
import com.arsr.arsr.fragment.FragmentTaskList;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.TestDataUtil;


public class MainActivity extends BasicActivity {
    private DrawerLayout mDrawerLayout;//根布局

    private AdapterMainFragmentPager pagerAdapter;//碎片页面适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //状态栏更换
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //滑动菜单
        mDrawerLayout = findViewById(R.id.drawerLayout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//利用返回按钮做导航按钮
            actionBar.setHomeAsUpIndicator(R.drawable.icon_menu);
        }
        //主界面两个子页面初始化
        pagerAdapter = new AdapterMainFragmentPager(getSupportFragmentManager(), this);
        initPaperAdapter();
        ViewPager viewPager = findViewById(R.id.viewPager);
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
        AdapterNavigationRecyclerView adapter = new AdapterNavigationRecyclerView(TestDataUtil.getGroupNames());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //test
        test();
    }

    /**
     * 写一些测试用代码
     */
    private void test() {

        DBUtil.initCache();
/*        //category
        CategoryDAO.insert("CTE-6");
        CategoryDAO.insert("Math");
        CategoryDAO.insert("考研");
        CategoryDAO.insert("计算机网络");
        //tag
        TagDAO.insert("Word","List","CTE-6","单词");
        TagDAO.insert("Reading 2016","Text","CTE-6","长难句");
        TagDAO.insert("Word","List","考研",null);
        TagDAO.insert("Chapter","Chapter","Math",null);
        TagDAO.insert("Chapter", "C", "计算机网络", null);
        //task
        //values("CTE-6_List_1",4,0,-1,-1,1);
        TaskDAO.insert("List_1",4,0,-1,-1,"CTE-6_Word");
        TaskDAO.insert("List_2",3,0,-1,-1,"CTE-6_Word");
        TaskDAO.insert("List_3",2,0,-1,-1,"CTE-6_Word");
        TaskDAO.insert("List_4",1,0,-1,-1,"CTE-6_Word");
        TaskDAO.insert("List_5",0,0,-1,-1,"CTE-6_Word");
        TaskDAO.insert("C_1",3,0,-1,-1,"Math_Chapter");
        TaskDAO.insert("C_2",3,0,-1,-1,"Math_Chapter");
        TaskDAO.insert("List_1",4,0,-1,-1,"考研_Word");
        TaskDAO.insert("List_2",2,0,-1,-1,"考研_Word");
        TaskDAO.insert("List_3",2,0,-1,-1,"考研_Word");
        CategoryDAO.display();
        TagDAO.display();
        TaskDAO.display();*/
        //更新
        DBUtil.get().execSQL("update category\n" +
                "set name = \"CESHI\"\n" +
                "where id = 1");

        CategoryDAO.display();
        TagDAO.display();
        TaskDAO.display();
        //删除
     /*   CategoryDAO.delete("CTE-6");
        CategoryDAO.display();
        TagDAO.display();*/
    }

    private void initPaperAdapter() {//cu
        pagerAdapter.addFragment(FragmentTaskList.newInstance(TestDataUtil.getGroups(), TestDataUtil.getChildren()));
        pagerAdapter.addFragment(FragmentCategoryList.newInstance(TestDataUtil.getGroups(), TestDataUtil.getChildren()));
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
