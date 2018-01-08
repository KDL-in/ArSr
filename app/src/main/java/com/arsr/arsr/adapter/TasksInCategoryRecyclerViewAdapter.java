package com.arsr.arsr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.util.DBUtil;
import com.xw.repo.BubbleSeekBar;

import java.util.List;

import drawthink.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import drawthink.expandablerecyclerview.bean.RecyclerViewData;
import drawthink.expandablerecyclerview.holder.BaseViewHolder;

/**
 * 分类管理界面adapter
 * Created by KundaLin on 18/1/8.
 */

public class TasksInCategoryRecyclerViewAdapter extends BaseRecyclerViewAdapter<String,String,TasksInCategoryRecyclerViewAdapter.MyViewHold> {
    private Context mContext;
    private LayoutInflater mInflater;
    List<RecyclerViewData> mData;

    public TasksInCategoryRecyclerViewAdapter(Context ctx, List<RecyclerViewData> datas) {
        super(ctx, datas);
        mContext = ctx;
        mInflater = LayoutInflater.from(ctx);
        mData = datas;
    }
    @Override
    public void setAllDatas(List<RecyclerViewData> allDatas) {
        super.setAllDatas(allDatas);
        mData = allDatas;
    }
    @Override
    public View getGroupView(ViewGroup parent) {
        return mInflater.inflate(R.layout.item_categorylist_child, parent, false);
    }

    @Override
    public View getChildView(ViewGroup parent) {
        return mInflater.inflate(R.layout.item_ticlist,parent,false);
    }

    @Override
    public MyViewHold createRealViewHolder(Context ctx, View view, int viewType) {
        MyViewHold hold = new MyViewHold(ctx, view, viewType);
        return hold;
    }

    @Override
    public void onBindGroupHolder(MyViewHold holder, int groupPos, int position, String groupData) {
        holder.tagTv.setText(DBUtil.getSubstringName(groupData,'_'));
    }

    @Override
    public void onBindChildpHolder(MyViewHold holder, int groupPos, int childPos, int position, String childData) {
        holder.childTv.setText(DBUtil.getSubstringName(childData,'_'));
    }

    class MyViewHold extends BaseViewHolder {
        //group
        TextView tagTv;
        //child
        TextView childTv;
        BubbleSeekBar seekBar;
        public MyViewHold(Context ctx, View itemView, int viewType) {
            super(ctx, itemView, viewType);
            tagTv = itemView.findViewById(R.id.tv_categoryList_childTittle);
            childTv = itemView.findViewById(R.id.tv_ticList_childTittle);
            seekBar = itemView.findViewById(R.id.bubbleSeekBar);
        }

        @Override
        public int getChildViewResId() {
            return R.id.linearLayout_ticlist_child;
        }

        @Override
        public int getGroupViewResId() {
            return R.id.linearLayout_categoryList_child;
        }
    }

}
