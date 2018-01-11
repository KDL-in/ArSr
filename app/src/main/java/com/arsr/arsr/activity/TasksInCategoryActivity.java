package com.arsr.arsr.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.arsr.arsr.MyApplication;
import com.arsr.arsr.R;
import com.arsr.arsr.adapter.TasksInCategoryRecyclerViewAdapter;
import com.arsr.arsr.db.Category;
import com.arsr.arsr.fragment.FragmentTaskList;
import com.arsr.arsr.listener.OnTicListClickListener;
import com.arsr.arsr.listener.OnTicListLongClickListener;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.ListUtil;
import com.arsr.arsr.util.LogUtil;
import com.arsr.arsr.util.ToastUtil;
import com.arsr.arsr.util.UIDataUtil;
import com.bumptech.glide.Glide;

public class TasksInCategoryActivity extends BasicActivity {

    private String categoryName;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ticlist_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item_ticlist_addTag:
                //自定义布局
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_addtag, null);
                //相关view
                final EditText tagNameEdit = view.findViewById(R.id.edit_addTag_name);
                final EditText tagPrefixEdit = view.findViewById(R.id.edit_addTag_prefix);
                final EditText tagNote = view.findViewById(R.id.edit_addTag_note);
                UIDataUtil.setConstrain(tagNameEdit, tagPrefixEdit);
                //弹出窗口
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(view);
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {//确认添加
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String name = tagNameEdit.getText().toString().trim();
                        final String prefix = tagPrefixEdit.getText().toString().trim();
                        final String note = tagNote.getText().toString().trim();
                        if (UIDataUtil.checkEmpty(name, prefix)) {
                            ToastUtil.makeToast("名字、前缀不能为空");
                            return;
                        }
                        final String category = categoryName;

                        ToastUtil.makeSnackbar(collapsingToolbarLayout, "确定插入标签" + name, "撤销插入", new ToastUtil.OnSnackbarListener() {
                            @Override
                            public void onUndoClick() {

                            }

                            @Override
                            public void onDismissed(int event) {
                                if (event == BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_ACTION) {
                                    return;
                                }
                                long id = DBUtil.tagDAO.insert(name, prefix, category, note);
                                if (id == -1) {
                                    ToastUtil.makeToast("添加失败，标签已存在");
                                    return;
                                }
                                UIDataUtil.updateUIData(UIDataUtil.TYPE_TAG_CHANGED);//插入
                            }

                            @Override
                            public void onShown() {

                            }
                        });


                    }
                });
                builder.show();
                break;
            case R.id.item_ticlist_deleteCategory:
                ToastUtil.makeDialog(this,"删除分类连同所有子项，不可恢复", new ToastUtil.OnAlterDialogListener() {
                    @Override
                    public void onPositiveButtonClick(DialogInterface dialog, int which) {
                        DBUtil.categoryDAO.delete(categoryName);
                        UIDataUtil.remove(UIDataUtil.KEY_TASKS_CATEGORY);
                        UIDataUtil.updateUIData(UIDataUtil.TYPE_CATEGORY_CHANGE);
                        finish();
                    }
                });
                break;
        }
        return true;
    }

    private CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_in_category);
        //获取数据
        Intent intent = getIntent();
        categoryName = intent.getStringExtra("category");
        //状态栏更换
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //滑动菜单
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //设置task名
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle(categoryName+"任务进度");
        //设置图片
        ImageView imageView = findViewById(R.id.img_ticUI_bg);
        Glide.with(this).load(R.drawable.task_background).into(imageView);
        //初始化列表
        TasksInCategoryRecyclerViewAdapter adapter = ListUtil.getTICListAdapter(this, categoryName);
        //-监听
        FragmentTaskList fragment = FragmentTaskList.newInstance(adapter);
        fragment.setOnItemClickListener(new OnTicListClickListener(this, adapter));
        fragment.setOnItemLongClickListener(new OnTicListLongClickListener(this, adapter));

        loadFragment(fragment);
    }

    @Override
    protected void onDestroy() {
        LogUtil.d("on destroy");
        UIDataUtil.remove(UIDataUtil.KEY_TASKS_CATEGORY);
        super.onDestroy();
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
