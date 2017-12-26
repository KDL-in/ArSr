package com.arsr.arsr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.activity.TaskActivity;
import com.arsr.arsr.entity.Child;
import com.arsr.arsr.entity.Group;

import java.util.ArrayList;
import java.util.List;

import drawthink.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import drawthink.expandablerecyclerview.bean.RecyclerViewData;
import drawthink.expandablerecyclerview.holder.BaseViewHolder;

/**
 * Created by KundaLin on 17/12/26.
 */

public class AdapterCategoryListRecyclerView extends BaseRecyclerViewAdapter<Group,Child,AdapterCategoryListRecyclerView.VHCategoryRecyclerView> {
    private Context mContext;
    private LayoutInflater mInflater;

    public AdapterCategoryListRecyclerView(Context ctx, List<RecyclerViewData> datas) {
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
        return mInflater.inflate(R.layout.item_categorylist_group, parent, false);
    }
    /**
     * 为任务（child）的时候，createViewHolder中被调用，加载布局
     *
     * @param parent
     * @return 加载之后的view
     */
    @Override
    public View getChildView(ViewGroup parent) {
        return mInflater.inflate(R.layout.item_categorylist_child, parent, false);
    }
    /**
     * createViewHolder中被调用，使用自定义holder创建出holder
     * todo 监听
     *
     * @param ctx
     * @param view
     * @param viewType
     * @return 自定义holder对象
     */
    @Override
    public VHCategoryRecyclerView createRealViewHolder(Context ctx, View view, int viewType) {
        VHCategoryRecyclerView holder = new VHCategoryRecyclerView(ctx, view, viewType);
        if (viewType == AdapterTaskListRecyclerView.VHTaskListRecyclerView.VIEW_TYPE_CHILD) {
            holder.childTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskActivity.actionStart(mContext);
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindGroupHolder(VHCategoryRecyclerView holder, int groupPos, int position, Group groupData) {
        holder.categoryTextView.setText(groupData.getName());
    }

    @Override
    public void onBindChildpHolder(VHCategoryRecyclerView holder, int groupPos, int childPos, int position, Child childData) {
        holder.childTextView.setText(childData.getName());
    }

    public static List<RecyclerViewData> initExListData(List<Group> groups, List<List<Child>> children) {
        List<RecyclerViewData> taskListData = new ArrayList<>();
        int groupSize = groups.size();
        for (int i = 0; i < groupSize; i++) {
            taskListData.add(new RecyclerViewData(groups.get(i), children.get(i), false));
        }
        return taskListData;
    }

    /**
     * 每一个类别名和任务公用的一个viewHolder，具体区分使用viewType
     * todo 等待抽象
     * Created by KundaLin on 17/12/22.
     */

    class VHCategoryRecyclerView extends BaseViewHolder {
        //group的数据
        TextView categoryTextView;
        //child的数据
        TextView childTextView;

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
}
