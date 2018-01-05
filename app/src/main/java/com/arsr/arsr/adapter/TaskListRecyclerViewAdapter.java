package com.arsr.arsr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.arsr.arsr.R;
import com.arsr.arsr.adapter.holder.VHTaskListRecyclerView;
import com.arsr.arsr.db.Category;
import com.arsr.arsr.db.Task;
import com.arsr.arsr.listener.OnFeelRadioCheckedChangeListener;
import com.arsr.arsr.util.DBUtil;

import java.util.List;

import drawthink.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import drawthink.expandablerecyclerview.bean.RecyclerViewData;

import static drawthink.expandablerecyclerview.holder.BaseViewHolder.VIEW_TYPE_CHILD;

/**
 * 任务列表的适配器，具体参数查看文档
 * 具体来说，这个用recyclerView实现的二级list的原理就是，通过标记group或child区分item的类型，
 * 进行不同操作，最后再加入展开收缩管理，以及定义好数据接口
 * Created by KundaLin on 17/12/22
 * 参数说明.
 * T :group  data
 * S :child  data
 * VH :VHCategoryRecyclerView
 */

public class TaskListRecyclerViewAdapter extends BaseRecyclerViewAdapter<String, String, VHTaskListRecyclerView> {

    private Context mContext;
    private LayoutInflater mInflater;
    List<RecyclerViewData> mData;

    public TaskListRecyclerViewAdapter(Context ctx, List<RecyclerViewData> datas) {
        super(ctx, datas);
        mContext = ctx;
        mInflater = LayoutInflater.from(ctx);
        mData = datas;
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
     *
     * @param ctx
     * @param view
     * @param viewType
     * @return 自定义holder对象
     */
    @Override
    public VHTaskListRecyclerView createRealViewHolder(Context ctx, View view, int viewType) {
        view.setTag(false);
        VHTaskListRecyclerView holder = new VHTaskListRecyclerView(ctx, view, viewType);
        if (viewType == VIEW_TYPE_CHILD) {
            RadioGroup radioGroup = view.findViewById(R.id.rdioGroup);
            radioGroup.setOnCheckedChangeListener(new OnFeelRadioCheckedChangeListener(this,view));
        }
        return holder;
    }

    /**
     * onBindHolder中调用，如果是group则绑定以下的操作
     */
    @Override
    public void onBindGroupHolder(VHTaskListRecyclerView holder, int groupPos, int position, String groupData) {
        holder.groupTextView.setText(groupData);
    }

    /**
     * onBindHolder中调用，如果是child则绑定以下操作
     */
    @Override
    public void onBindChildpHolder(VHTaskListRecyclerView holder, int groupPos, int childPos, int position, String childData) {
        Task task = getTask(groupPos, childPos);
        if (task.getDayToRecall() == -1 || task.getDayToAssist() == -1) {
            holder.childTextView.setSelected(true);
            holder.childIconImg.setSelected(true);
        } else {
            holder.childTextView.setSelected(false);
            holder.childIconImg.setSelected(false);
        }
        holder.childTextView.setText(DBUtil.getSubstringName(childData, ' '));
        holder.childCategory.setText(DBUtil.getSubstringCategory(childData, ' '));

    }


    public void remove(int position,int groupPosition, int childPosition) {
//        mData.get(groupPosition).removeChild(childPosition);
//        notifyRecyclerViewData();

    }


    public Task getTask(int groupPosition, int childPosition) {
        return DBUtil.getTask(DBUtil.packing((String)mData.get(groupPosition).getChild(childPosition)));
    }

    public Category getCategory(int groupPosition) {
        return DBUtil.getCategory((String) mData.get(groupPosition).getGroupData());
    }
}
