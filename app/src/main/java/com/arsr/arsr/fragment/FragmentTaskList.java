package com.arsr.arsr.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arsr.arsr.R;
import com.arsr.arsr.adapter.TaskListRecyclerViewAdapter;
import com.arsr.arsr.db.Task;
import com.arsr.mexpandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import com.arsr.mexpandablerecyclerview.listener.OnRecyclerViewListener;

import static com.arsr.mexpandablerecyclerview.holder.BaseViewHolder.VIEW_TYPE_CHILD;


/**
 * 任务列表碎片
 */
public class FragmentTaskList extends Fragment {

    private BaseRecyclerViewAdapter adapter;
    //listener
    private OnRecyclerViewListener.OnItemClickListener listener;
    private OnRecyclerViewListener.OnItemLongClickListener longListener;
    private ItemTouchHelper.SimpleCallback simpleCallBack;

    public FragmentTaskList() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    private FragmentTaskList(BaseRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    public static FragmentTaskList newInstance(BaseRecyclerViewAdapter adapter) {
        FragmentTaskList fragment = new FragmentTaskList(adapter);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        //初始化任务列表
        RecyclerView listRecyclerView = view.findViewById(R.id.recyclerView_mainUI_list);
//        adapter = new TaskListRecyclerViewAdapter(getActivity(), taskListData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter.setOnItemClickListener(listener);
        adapter.setOnItemLongClickListener(longListener);
        listRecyclerView.setAdapter(adapter);
        listRecyclerView.setLayoutManager(layoutManager);
        if (simpleCallBack != null) {
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallBack);
            itemTouchHelper.attachToRecyclerView(listRecyclerView);
        }

        return view;
    }

    public void setTouchItemCallback(ItemTouchHelper.SimpleCallback callback) {
        simpleCallBack = callback;
    }
    public void setOnItemClickListener(OnRecyclerViewListener.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnRecyclerViewListener.OnItemLongClickListener listener) {
        this.longListener = listener;
    }


}
