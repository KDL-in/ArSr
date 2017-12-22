package com.arsr.arsr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.security.acl.Group;

/**
 * 任务列表的适配器
 * Created by KundaLin on 17/12/22.
 */

public class TaskListAdapterBak extends BaseExpandableListAdapter {
    //临时数据
    public String[] groupStrings = {"西游记", "水浒传", "三国演义", "红楼梦"};
    public String[][] childStrings = {
            {"唐三藏", "孙悟空", "猪八戒", "沙和尚"},
            {"宋江", "林冲", "李逵", "鲁智深"},
            {"曹操", "刘备", "孙权", "诸葛亮", "周瑜"},
            {"贾宝玉", "林黛玉", "薛宝钗", "王熙凤"}
    };
    private Context mContext;

    public TaskListAdapterBak(Context mContext, String[] groupStrings, String[][] childStrings) {
        this.mContext = mContext;
        this.groupStrings = groupStrings;
        this.childStrings = childStrings;
    }

    @Override
    public int getGroupCount() {
        return groupStrings.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return childStrings[i].length;
    }

    @Override
    public Object getGroup(int i) {
        return groupStrings[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        return childStrings[i][i1];
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.customview_category, viewGroup, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.nameTextView = view.findViewById(R.id.text_category_name);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }
        groupViewHolder.nameTextView.setText(groupStrings[i]);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.customview_task, viewGroup, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.nameTextView = view.findViewById(R.id.text_task_name);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }
        childViewHolder.nameTextView.setText(childStrings[i][i1]);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private class GroupViewHolder {
        public TextView nameTextView;
    }

    private class ChildViewHolder {
        public TextView nameTextView;
    }
}
