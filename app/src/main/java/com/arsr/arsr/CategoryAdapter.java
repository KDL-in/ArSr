package com.arsr.arsr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arsr.arsr.costumview.CategoryLayout;
import com.arsr.arsr.costumview.TaskLayout;
import com.arsr.arsr.costumview.TasksOfACategory;
import com.arsr.arsr.entity.Category;
import com.arsr.arsr.entity.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有任务循环列表的适配器
 * Created by KundaLin on 17/12/21.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context mContext;
    private List<Category> mCategories;

    public CategoryAdapter(List<Category> mCategories) {
        this.mCategories = mCategories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_tasks_of_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category category = mCategories.get(position);
        List<Task> tasks = category.getChildren();
        //判断是否已经取得子任务的布局
        if (holder.allTasksLayout.isEmpty()) {
            holder.tasksOfACategory.addTasks(tasks);
            holder.getAllTasksLayout();
        }
        //设置当前子项信息
        holder.categoryLayout.setCategoryProperties(category);//类别名字
        for (int i = 0; i < tasks.size(); i++) {//设置子任务信息
            holder.allTasksLayout.get(i).setTaskProperties(tasks.get(i));
        }
    }


    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TasksOfACategory tasksOfACategory;//根布局
        CategoryLayout categoryLayout;//类别的布局
        LinearLayout tasksLayout;//子任务的父布局
        List<TaskLayout> allTasksLayout;//所有子任务的布局集

        public ViewHolder(View itemView) {
            super(itemView);
            tasksOfACategory = (TasksOfACategory) itemView;
            categoryLayout = itemView.findViewById(R.id.layout_category);
            tasksLayout = itemView.findViewById(R.id.layout_tasks);
            allTasksLayout = new ArrayList<>();
        }

        /**
         * 取得子任务布局
         * 1. 取得所有子任务布局
         */
        public void getAllTasksLayout() {
            int count = tasksLayout.getChildCount();
            //取得子任务
            for (int i = 0; i < count; i++) {
                allTasksLayout.add((TaskLayout) tasksLayout.getChildAt(i));
            }


        }
    }
}
