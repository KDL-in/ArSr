package com.arsr.arsr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arsr.arsr.util.LogUtil;

import java.util.List;

import drawthink.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import drawthink.expandablerecyclerview.bean.RecyclerViewData;

/**
 * 任务列表的适配器，具体参数查看文档
 * 具体来说，这个用recyclerView实现的二级list的原理就是，通过标记group或child区分item的类型，
 * 进行不同操作，最后再加入展开收缩管理，以及定义好数据接口
 * Created by KundaLin on 17/12/22
 * 参数说明.
 * T :group  data
 * S :child  data
 * VH :ViewHolder
 */

public class TaskListAdapter extends BaseRecyclerViewAdapter<String, String, TaskListViewHolder> {
    private Context mContext;
    private List datas;
    private LayoutInflater mInflater;
    public TaskListAdapter(Context ctx, List<RecyclerViewData> datas) {
        super(ctx, datas);
        mContext = ctx;
        this.datas = datas;
        mInflater = LayoutInflater.from(ctx);
    }

    /**
     * 为类别（group）的时候，createViewHolder中被调用，加载布局
     * @param parent
     * @return 加载之后的view
     */
    @Override
    public View getGroupView(ViewGroup parent) {
        return mInflater.inflate(R.layout.customview_category,parent,false);
    }
    /**
     * 为任务（child）的时候，createViewHolder中被调用，加载布局
     * @param parent
     * @return 加载之后的view
     */
    @Override
    public View getChildView(ViewGroup parent) {
        return mInflater.inflate(R.layout.customview_task,parent,false);
    }

    /**
     * createViewHolder中被调用，使用自定义holder创建出holder
     * @param ctx
     * @param view
     * @param viewType
     * @return 自定义holder对象
     */
    @Override
    public TaskListViewHolder createRealViewHolder(Context ctx, View view, int viewType) {
        return new TaskListViewHolder(ctx,view,viewType);
    }

    /**
     * onBindHolder中调用，如果是group则绑定以下的操作
     * @param holder
     * @param groupPos
     * @param position
     * @param groupData
     */
    @Override
    public void onBindGroupHolder(TaskListViewHolder holder, int groupPos, int position, String groupData) {
        holder.categoryTextView.setText(groupData);
    }

    /**
     * onBindHolder中调用，如果是child则绑定以下操作
     * @param holder
     * @param groupPos
     * @param childPos
     * @param position
     * @param childData
     */
    @Override
    public void onBindChildpHolder(TaskListViewHolder holder, int groupPos, int childPos, int position, String childData) {
        holder.childTextView.setText(childData);
    }
}
