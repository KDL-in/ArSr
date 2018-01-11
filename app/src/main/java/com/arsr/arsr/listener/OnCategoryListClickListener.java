package com.arsr.arsr.listener;

import android.view.View;

import com.arsr.arsr.activity.MainActivity;
import com.arsr.arsr.adapter.CategoryListRecyclerViewAdapter;
import com.arsr.arsr.costumview.AddTaskBottomDialog;
import com.arsr.arsr.db.Tag;
import com.arsr.mexpandablerecyclerview.listener.OnRecyclerViewListener;


/**
 * 分类列表短按监听
 * Created by KundaLin on 18/1/5.
 */

public class OnCategoryListClickListener implements OnRecyclerViewListener.OnItemClickListener {
    private MainActivity context;
    private CategoryListRecyclerViewAdapter adapter;
    public OnCategoryListClickListener(MainActivity mainActivity, CategoryListRecyclerViewAdapter categoryAdapter) {
        context = mainActivity;
        this.adapter = categoryAdapter;
    }

    @Override
    public void onGroupItemClick(int position, int groupPosition, View view) {

    }

    @Override
    public void onChildItemClick(int position, int groupPosition, int childPosition, View view) {
        AddTaskBottomDialog dialog = new AddTaskBottomDialog();
        dialog.show(context.getSupportFragmentManager());
        dialog.setData(adapter.getChildData(groupPosition,childPosition));
    }
}
