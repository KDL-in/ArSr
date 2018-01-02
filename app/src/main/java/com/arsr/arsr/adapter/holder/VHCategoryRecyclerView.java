package com.arsr.arsr.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.arsr.arsr.R;

import drawthink.expandablerecyclerview.holder.BaseViewHolder;

/**
 * 每一个类别名和任务公用的一个viewHolder，具体区分使用viewType
 * todo 等待抽象
 * Created by KundaLin on 17/12/22.
 */

public class VHCategoryRecyclerView extends BaseViewHolder {
    //group的数据
    public TextView categoryTextView;
    //child的数据
    public TextView childTextView;

    public VHCategoryRecyclerView(Context ctx, View itemView, int viewType) {
        super(ctx, itemView, viewType);
        //        LogUtil.d("type",itemView.getClass().toString());
        switch (viewType) {
            case VIEW_TYPE_PARENT:
                categoryTextView = itemView.findViewById(R.id.tv_categoryList_groupTitle);
                break;
            case VIEW_TYPE_CHILD:
                childTextView = itemView.findViewById(R.id.tv_categoryList_childTittle);
                break;
        }
    }

    @Override
    public int getChildViewResId() {
        return R.id.linearLayout_categoryList_child;
    }

    @Override
    public int getGroupViewResId() {
        return R.id.linearLayout_categoryList_group;
    }
}