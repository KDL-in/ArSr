package com.arsr.arsr.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arsr.arsr.R;

import drawthink.expandablerecyclerview.holder.BaseViewHolder;

/**
     * 每一个类别名和任务公用的一个viewHolder，具体区分使用viewType
     * todo 等待抽象
     * Created by KundaLin on 17/12/22.
     */

    public class VHTaskListRecyclerView extends BaseViewHolder {
        //group的数据
        public TextView categoryTextView;
        public ImageView expandImgBtn;
        //child的数据
        public TextView childTextView;

        public VHTaskListRecyclerView(Context ctx, View itemView, int viewType) {
            super(ctx, itemView, viewType);
            //        LogUtil.d("type",itemView.getClass().toString());
            switch (viewType) {
                case VIEW_TYPE_PARENT:
                    categoryTextView = itemView.findViewById(R.id.tv_taskList_groupTitle);
                    expandImgBtn = itemView.findViewById(R.id.img_taskList_groupImgButton);
                    break;
                case VIEW_TYPE_CHILD:
                    childTextView = itemView.findViewById(R.id.tv_taskList_childTittle);
                    break;
            }
        }

        @Override
        public int getChildViewResId() {
            return R.id.cardView_taskList_child;
        }

        @Override
        public int getGroupViewResId() {
            return R.id.cardView_taskList_group;
        }
    }