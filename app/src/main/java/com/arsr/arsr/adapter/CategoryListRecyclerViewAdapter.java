package com.arsr.arsr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arsr.arsr.R;
import com.arsr.arsr.adapter.holder.VHCategoryRecyclerView;
import com.arsr.arsr.listener.OnCategoryAddButtonListener;
import com.arsr.arsr.util.DBUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import drawthink.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import drawthink.expandablerecyclerview.bean.RecyclerViewData;

import static drawthink.expandablerecyclerview.holder.BaseViewHolder.VIEW_TYPE_PARENT;

/**
 * Created by KundaLin on 17/12/26.
 */

public class CategoryListRecyclerViewAdapter extends BaseRecyclerViewAdapter<String,String,VHCategoryRecyclerView> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<RecyclerViewData>mData;

    public CategoryListRecyclerViewAdapter(Context ctx, List<RecyclerViewData> datas) {
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
     */
    @Override
    public VHCategoryRecyclerView createRealViewHolder(Context ctx, View view, int viewType) {
        VHCategoryRecyclerView holder = new VHCategoryRecyclerView(ctx, view, viewType);
        if (viewType == VIEW_TYPE_PARENT) {
            ImageView imageView = view.findViewById(R.id.img_categoryList_groupImgButton);
            imageView.setOnClickListener(new OnCategoryAddButtonListener(view,mContext));
        }
        return holder;
    }

    @Override
    public void onBindGroupHolder(VHCategoryRecyclerView holder, int groupPos, int position, String groupData) {
        holder.categoryTextView.setText(groupData);
    }

    @Override
    public void onBindChildpHolder(VHCategoryRecyclerView holder, int groupPos, int childPos, int position, String childData) {
        holder.childTextView.setText(childData.substring(childData.indexOf(' ')));
    }


    public String getGroupData(int groupPosition) {
        return (String) mData.get(groupPosition).getGroupData();
    }

    public String getChildData(int groupPosition, int childPosition) {
        return (String) mData.get(groupPosition).getChild(childPosition);
    }

    @Override
    public void setAllDatas(List<RecyclerViewData> allDatas) {
        super.setAllDatas(allDatas);
        mData = allDatas;
    }
}
