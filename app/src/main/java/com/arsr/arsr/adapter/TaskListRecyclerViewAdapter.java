package com.arsr.arsr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.db.Category;
import com.arsr.arsr.db.Task;
import com.arsr.arsr.listener.OnFeelRadioCheckedChangeListener;
import com.arsr.arsr.util.DBUtil;
import com.arsr.mexpandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import com.arsr.mexpandablerecyclerview.bean.RecyclerViewData;
import com.arsr.mexpandablerecyclerview.holder.BaseViewHolder;

import java.util.List;


import static com.arsr.mexpandablerecyclerview.holder.BaseViewHolder.VIEW_TYPE_CHILD;


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

public class TaskListRecyclerViewAdapter extends BaseRecyclerViewAdapter<String, String, TaskListRecyclerViewAdapter.VHTaskListRecyclerView> {

    private Context mContext;
    private LayoutInflater mInflater;
    List<RecyclerViewData> mData;

    public TaskListRecyclerViewAdapter(Context ctx, List<RecyclerViewData> datas) {
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
        if (task!=null&&(task.getDayToRecall() == -1 || task.getDayToAssist() == -1)) {
            holder.view.setTag(true);
            holder.childTextView.setSelected(true);
            holder.childIconImg.setSelected(true);
        } else {
            holder.childTextView.setSelected(false);
            holder.childIconImg.setSelected(false);
        }
        holder.childTextView.setText(DBUtil.getSubstringName((String) mData.get(groupPos).getChild(childPos),' '));
        holder.childCategory.setText((String) mData.get(groupPos).getGroupData());

    }


    public Task getTask(int groupPosition, int childPosition) {
        return DBUtil.taskDAO.get(DBUtil.packing((String)mData.get(groupPosition).getChild(childPosition)));
    }

    public Category getCategory(int groupPosition) {
        return DBUtil.getCategory((String) mData.get(groupPosition).getGroupData());
    }

    public int getAdapterPosition(String groupName) {
        return getGroupAdapterPosition(groupName);
    }

    public int insertChild(int position, String childName) {
        int gp = getGroupPosition(position-1);
        mData.get(gp).getGroupItem().getChildDatas().add(0,childName);
        return notifyChildInserted(position,childName);
    }
    /**
     * 删除position处的元素
     */
    public String delete(int position) {
        int gp = getGroupPosition(position);
        int cp = getChildPosition(gp,position);
        mData.get(gp).getGroupItem().getChildDatas().remove(cp);

        return notifyChildRemoved(position);
    }


    /**
     * 每一个类别名和任务公用的一个viewHolder，具体区分使用viewType
     * Created by KundaLin on 17/12/22.
     */

    public static class VHTaskListRecyclerView extends BaseViewHolder {
        public View view;
        //group的数据
        public TextView groupTextView;
        public ImageView expandImgBtn;
        //child的数据
        public TextView childTextView;
        public ImageView childIconImg;
        public TextView childCategory;

        public VHTaskListRecyclerView(Context ctx, View itemView, int viewType) {
            super(ctx, itemView, viewType);
            view = itemView;
            //        LogUtil.d("type",itemView.getClass().toString());
            switch (viewType) {
                case VIEW_TYPE_PARENT:
                    groupTextView = itemView.findViewById(R.id.tv_taskList_groupTitle);
                    expandImgBtn = itemView.findViewById(R.id.img_taskList_groupImgButton);
                    break;
                case VIEW_TYPE_CHILD:
                    childTextView = itemView.findViewById(R.id.tv_taskList_childTittle);
                    childIconImg = itemView.findViewById(R.id.img_taskList_icon);
                    childCategory = itemView.findViewById(R.id.tv_taskList_childCategory);
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
}
