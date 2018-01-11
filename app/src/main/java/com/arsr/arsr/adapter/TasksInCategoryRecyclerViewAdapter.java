package com.arsr.arsr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.ToastUtil;
import com.arsr.mexpandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import com.arsr.mexpandablerecyclerview.bean.RecyclerViewData;
import com.arsr.mexpandablerecyclerview.holder.BaseViewHolder;
import com.xw.repo.BubbleSeekBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 分类管理界面adapter
 * Created by KundaLin on 18/1/8.
 */

public class TasksInCategoryRecyclerViewAdapter extends BaseRecyclerViewAdapter<String,String,TasksInCategoryRecyclerViewAdapter.MyViewHold> {
    private Context mContext;
    private LayoutInflater mInflater;
    List<RecyclerViewData> mData;
    Map<String,Integer> nameToTimes;
    int[]colors;
    private String category;

    public TasksInCategoryRecyclerViewAdapter(Context ctx, List<RecyclerViewData> datas, String categoryName) {
        super(ctx, datas);
        mContext = ctx;
        mInflater = LayoutInflater.from(ctx);
        mData = datas;
        nameToTimes = new HashMap<>();
        colors=ctx.getResources().getIntArray(R.array.seekBarColors);
        category = categoryName;

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
        holder.childTv.setText(DBUtil.getSubstringName(childData, '_').replaceAll("_", " "));
        if (nameToTimes.containsKey(childData)) {
            holder.seekBar.setProgress(nameToTimes.get(childData));
        } else {
            int times = DBUtil.taskDAO.quaryTimes(childData);
            holder.seekBar.setProgress(times);
            nameToTimes.put(childData, times);
            holder.seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                private int fp = -1;

                @Override
                public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                    bubbleSeekBar.setSecondTrackColor(colors[progress]);
                    if (fp == -1) fp = progress;
                }

                @Override
                public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                    bubbleSeekBar.setProgress(fp);
                    fp = -1;
                }

                @Override
                public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                }
            });
        }
        holder.seekBar.setSecondTrackColor(colors[nameToTimes.get(childData)]);
        holder.seekBar.setThumbColor(colors[nameToTimes.get(childData)]);
    }

    public String getChildData(int groupPosition, int position) {
        return (String) mData.get(groupPosition).getChild(position);
    }

    public String getGroupData(int groupPosition) {
        return (String) mData.get(groupPosition).getGroupData();
    }

    public String getCategory() {
        return category;
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
