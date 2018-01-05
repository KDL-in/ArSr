package com.arsr.arsr.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arsr.arsr.activity.TasksInCategoryActivity;
import com.arsr.arsr.R;

import java.util.List;

/**
 * Created by KundaLin on 17/12/25.
 */

public class NavigationRecyclerViewAdapter extends RecyclerView.Adapter<NavigationRecyclerViewAdapter.MyViewHolder>{
    private List<String> mData;

    public NavigationRecyclerViewAdapter(List<String> mData) {
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_navigation_category, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        holder.textName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//todo 应该监听布局
                TasksInCategoryActivity.startAction("");
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textName.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        public MyViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_category_name_nav);
        }
    }

}
