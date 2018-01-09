package com.arsr.arsr.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arsr.arsr.R;

import java.util.List;

/**
 * Created by KundaLin on 17/12/25.
 */

public class NavigationRecyclerViewAdapter extends RecyclerView.Adapter<NavigationRecyclerViewAdapter.MyViewHolder>{
    private List<String> mData;
    private OnClickListener listener;

    public NavigationRecyclerViewAdapter(List<String> mData) {
        this.mData = mData;
    }
    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_navigation_category, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClickListener(v);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onItemLongClickListener(v);
                return true;
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

    public void setOnItemClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void add(String name) {
        mData.add(name);
        notifyItemInserted(mData.size());
    }

    public void setAllDatas(List<String> allDatas) {
        this.mData = allDatas;
        notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textName = itemView.findViewById(R.id.tv_nav_categoryName);
        }
    }
    public interface OnClickListener {
        void onItemClickListener(View v);

        void onItemLongClickListener(View v);
    }
}
