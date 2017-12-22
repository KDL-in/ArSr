package com.arsr.arsr;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.arsr.arsr.util.LogUtil;

import drawthink.expandablerecyclerview.holder.BaseViewHolder;

/**
 * 每一个类别名和任务公用的一个viewHolder，具体区分使用viewType
 * Created by KundaLin on 17/12/22.
 */

public class TaskListViewHolder extends BaseViewHolder {
    //group的数据
    TextView categoryTextView;
    //child的数据
    TextView childTextView;
    public TaskListViewHolder(Context ctx, View itemView, int viewType) {
        super(ctx, itemView, viewType);
//        LogUtil.d("type",itemView.getClass().toString());
        switch (viewType) {
            case VIEW_TYPE_PARENT:
                categoryTextView = itemView.findViewById(R.id.text_category_name);
                break;
            case VIEW_TYPE_CHILD:
                childTextView = itemView.findViewById(R.id.text_task_name);
                break;
        }
    }

    @Override
    public int getChildViewResId() {
        return R.id.child;
    }

    @Override
    public int getGroupViewResId() {
        return R.id.group;
    }
}
