/*
package com.arsr.arsr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arsr.arsr.R;
import com.arsr.arsr.adapter.CategoryListRecyclerViewAdapter;

import java.util.List;

import drawthink.expandablerecyclerview.bean.RecyclerViewData;


*/
/**
 * 类别碎片
 * 等待加入内容
 * todo 等待抽象
 *//*

public class FragmentCategoryList extends Fragment {
    // 数据
    private static List<RecyclerViewData> categoryListData;


    public FragmentCategoryList() {
        // Required empty public constructor
    }

    public static FragmentCategoryList newInstance(List<RecyclerViewData>list) {
        FragmentCategoryList fragment = new FragmentCategoryList();
        categoryListData = list;
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
        View view = inflater.inflate(R.layout.fragment_categorylist, container, false);
        //初始化任务列表
        RecyclerView recyclerCategoryList = view.findViewById(R.id.recyclerView_mainUI_categoryList);
        CategoryListRecyclerViewAdapter listAdapter = new CategoryListRecyclerViewAdapter(getActivity(), categoryListData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerCategoryList.setAdapter(listAdapter);
        recyclerCategoryList.setLayoutManager(layoutManager);
        return view;
    }

}
*/
