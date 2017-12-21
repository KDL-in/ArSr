package com.arsr.arsr;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arsr.arsr.costumview.CategoryLayout;
import com.arsr.arsr.entity.Category;
import com.arsr.arsr.entity.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BasicActivity {
    private DrawerLayout mDrawerLayout;//根布局
    private List<Category> testList;//测试用数据列表

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
        //主界面循环列表
        initTestList();
        RecyclerView recyclerView = findViewById(R.id.recycler_today);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        CategoryAdapter adapter = new CategoryAdapter(testList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化测试数据
     */
    private void initTestList() {
        testList = new ArrayList<>();
        Category category = new Category();
        category.setName("考研单词");
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setName("List-1");
        Task task2 = new Task();
        task2.setName("List-2");
        tasks.add(task1);
        tasks.add(task2);
        category.setChildren(tasks);
        testList.add(category);
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
