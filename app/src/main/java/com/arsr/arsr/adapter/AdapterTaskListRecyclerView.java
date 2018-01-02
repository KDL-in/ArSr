package com.arsr.arsr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.activity.TaskActivity;
import com.arsr.arsr.adapter.holder.VHTaskListRecyclerView;
import com.arsr.arsr.entity.Child;
import com.arsr.arsr.entity.Group;

import java.util.ArrayList;
import java.util.List;

import drawthink.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import drawthink.expandablerecyclerview.bean.GroupItem;
import drawthink.expandablerecyclerview.bean.RecyclerViewData;
import drawthink.expandablerecyclerview.holder.BaseViewHolder;

/**
 * 任务列表的适配器，具体参数查看文档
 * 具体来说，这个用recyclerView实现的二级list的原理就是，通过标记group或child区分item的类型，
 * 进行不同操作，最后再加入展开收缩管理，以及定义好数据接口
 * todo 等待抽象 几个列表都差不多 只有监听以及一些小区别
 * Created by KundaLin on 17/12/22
 * 参数说明.
 * T :group  data
 * S :child  data
 * VH :VHCategoryRecyclerView
 */

public class AdapterTaskListRecyclerView extends BaseRecyclerViewAdapter<String, String, VHTaskListRecyclerView> {

    private Context mContext;
    private LayoutInflater mInflater;

    public AdapterTaskListRecyclerView(Context ctx, List<RecyclerViewData> datas) {
        super(ctx, datas);
        mContext = ctx;
        mInflater = LayoutInflater.from(ctx);
    }

    /**
     * 为类别（group）的时候，createViewHolder中被调用，加载布局
     *
     * @param parent
     * @return 加载之后的view
     */
    @Override
    public View getGroupView(ViewGroup parent) {
        return mInflater.inflate(R.layout.item_tasklist_group, parent, false);
    }

    /**
     * 为任务（child）的时候，createViewHolder中被调用，加载布局
     *
     * @param parent
     * @return 加载之后的view
     */
    @Override
    public View getChildView(ViewGroup parent) {
        return mInflater.inflate(R.layout.item_tasklist_child, parent, false);
    }

    /**
     * createViewHolder中被调用，使用自定义holder创建出holder
     * @param ctx
     * @param view
     * @param viewType
     * @return 自定义holder对象
     */
    @Override
    public VHTaskListRecyclerView createRealViewHolder(Context ctx, View view, int viewType) {
        VHTaskListRecyclerView holder = new VHTaskListRecyclerView(ctx, view, viewType);
        return holder;
    }

    /**
     * onBindHolder中调用，如果是group则绑定以下的操作
     *
     * @param holder
     * @param groupPos
     * @param position
     * @param groupData
     */
    @Override
    public void onBindGroupHolder(VHTaskListRecyclerView holder, int groupPos, int position, String groupData) {
        holder.categoryTextView.setText(groupData);
    }

    /**
     * onBindHolder中调用，如果是child则绑定以下操作
     *
     * @param holder
     * @param groupPos
     * @param childPos
     * @param position
     * @param childData
     */
    @Override
    public void onBindChildpHolder(VHTaskListRecyclerView holder, int groupPos, int childPos, int position, String childData) {
        holder.childTextView.setText(childData);
    }


}
