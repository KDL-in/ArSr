package com.arsr.arsr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.activity.TaskActivity;

import java.util.List;

import drawthink.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import drawthink.expandablerecyclerview.bean.RecyclerViewData;
import drawthink.expandablerecyclerview.holder.BaseViewHolder;

/**
 * 任务列表的适配器，具体参数查看文档
 * 具体来说，这个用recyclerView实现的二级list的原理就是，通过标记group或child区分item的类型，
 * 进行不同操作，最后再加入展开收缩管理，以及定义好数据接口
 * Created by KundaLin on 17/12/22
 * 参数说明.
 * T :group  data
 * S :child  data
 * VH :MyViewHolder
 */

public class TaskListRecycleViewAdapter extends BaseRecyclerViewAdapter<String, String, TaskListRecycleViewAdapter.MyViewHolder> {
    private Context mContext;
    private List datas;
    private LayoutInflater mInflater;
    public TaskListRecycleViewAdapter(Context ctx, List<RecyclerViewData> datas) {
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
     * todo 监听
     * @param ctx
     * @param view
     * @param viewType
     * @return 自定义holder对象
     */
    @Override
    public MyViewHolder createRealViewHolder(Context ctx, View view, int viewType) {
        MyViewHolder holder = new MyViewHolder(ctx, view, viewType);
        if (viewType == MyViewHolder.VIEW_TYPE_CHILD) {
            holder.childTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskActivity.actionStart(mContext);
                }
            });
        }
        return holder;
    }

    /**
     * onBindHolder中调用，如果是group则绑定以下的操作
     * @param holder
     * @param groupPos
     * @param position
     * @param groupData
     */
    @Override
    public void onBindGroupHolder(MyViewHolder holder, int groupPos, int position, String groupData) {
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
    public void onBindChildpHolder(MyViewHolder holder, int groupPos, int childPos, int position, String childData) {
        holder.childTextView.setText(childData);
    }

    /**
     * 每一个类别名和任务公用的一个viewHolder，具体区分使用viewType
     * Created by KundaLin on 17/12/22.
     */

    class MyViewHolder extends BaseViewHolder {
        //group的数据
        TextView categoryTextView;
        //child的数据
        TextView childTextView;
        public MyViewHolder(Context ctx, View itemView, int viewType) {
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
}
