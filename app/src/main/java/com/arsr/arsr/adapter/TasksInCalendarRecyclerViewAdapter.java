package com.arsr.arsr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.db.Task;
import com.arsr.arsr.util.DBUtil;
import com.arsr.mexpandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import com.arsr.mexpandablerecyclerview.bean.RecyclerViewData;
import com.arsr.mexpandablerecyclerview.holder.BaseViewHolder;

import java.util.List;
import java.util.Map;

/**
 * 日历-任务界面适配器
 * Created by KundaLin on 18/1/11.
 */

public class TasksInCalendarRecyclerViewAdapter extends BaseRecyclerViewAdapter<String, String, TasksInCalendarRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    List<RecyclerViewData> mData;

    public TasksInCalendarRecyclerViewAdapter(Context ctx, List<RecyclerViewData> datas) {
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
    public ViewHolder createRealViewHolder(Context ctx, View view, int viewType) {
        view.setTag(false);
        ViewHolder holder = new ViewHolder(ctx, view, viewType);
        return holder;
    }
    /**
     * onBindHolder中调用，如果是group则绑定以下的操作
     */
    @Override
    public void onBindGroupHolder(ViewHolder holder, int groupPos, int position, String groupData) {
        holder.groupTextView.setText(groupData);
    }
    public Task getTask(int groupPosition, int childPosition) {
        return DBUtil.taskDAO.get(DBUtil.packing((String) mData.get(groupPosition).getChild(childPosition)));
    }

    /**
     * onBindHolder中调用，如果是child则绑定以下操作
     */
    @Override
    public void onBindChildpHolder(ViewHolder holder, int groupPos, int childPos, int position, String childData) {
        Task task = getTask(groupPos, childPos);
        if (task.getDayToRecall() == -1 || task.getDayToAssist() == -1) {
            holder.view.setTag(true);
            holder.childTextView.setSelected(true);
            holder.childIconImg.setSelected(true);
        } else {
            holder.childTextView.setSelected(false);
            holder.childIconImg.setSelected(false);
        }
        holder.childTextView.setText(DBUtil.getSubstringName(childData, ' '));
        holder.childCategory.setText(DBUtil.getSubstringCategory(childData, ' '));

    }

    public void removeChild(int position) {
        int gp = getGroupPosition(position);
        int cp = getChildPosition(gp,position);
        mData.get(gp).removeChild(cp);
        notifyChildRemoved(position);
    }



    public class ViewHolder extends BaseViewHolder {
        public View view;
        //group的数据
        public TextView groupTextView;
        public ImageView expandImgBtn;
        //child的数据
        public TextView childTextView;
        public ImageView childIconImg;
        public TextView childCategory;

        public ViewHolder(Context ctx, View itemView, int viewType) {
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

        public String getRealTaskName() {
            String name = childCategory.getText() + " " + childTextView.getText();
            //空格
            return name.replaceAll(" ","_");
        }
    }
}
