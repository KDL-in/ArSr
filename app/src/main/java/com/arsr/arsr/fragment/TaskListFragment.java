package com.arsr.arsr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arsr.arsr.R;
import com.arsr.arsr.adapter.TaskListRecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;

import drawthink.expandablerecyclerview.bean.RecyclerViewData;


/**
 * 任务列表碎片
 */
public class TaskListFragment extends Fragment {

    // 数据
    private static List<RecyclerViewData> taskListData;


    public TaskListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param groupNames  group名字.
     * @param childNames  child名字.
     * @return A new instance of fragment TaskListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskListFragment newInstance(String[] groupNames,String[][] childNames) {
        TaskListFragment fragment = new TaskListFragment();
        taskListData=initExListData(groupNames,childNames);
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
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        //初始化任务列表
        RecyclerView recyclerViewTaskList = view.findViewById(R.id.recycler_task_list);
        TaskListRecycleViewAdapter listAdapter = new TaskListRecycleViewAdapter(getActivity(), taskListData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewTaskList.setAdapter(listAdapter);
        recyclerViewTaskList.setLayoutManager(layoutManager);
        return view;
    }

    /**
     * 封装初始化数据(任务列表)
     * todo 之后这里应该要改，数据不只是名字,可能要封装成工具
     *
     * @param groupNames
     * @param childNames
     */
    public static List<RecyclerViewData> initExListData(String[] groupNames, String[][] childNames) {
        List<RecyclerViewData> taskListData = new ArrayList<>();
        int groupLength = groupNames.length;
        int childLength = childNames.length;

        for (int i = 0; i < groupLength; i++) {
            List<String> childData = new ArrayList<>();
            for (int j = 0; j < childLength; j++) {
                childData.add(childNames[i][j]);
            }
            taskListData.add(new RecyclerViewData(groupNames[i], childData, true));
        }
        return taskListData;
    }
   /*  监听方面的框架
   private OnFragmentInteractionListener mListener;
    Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    *//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*
    public interface OnFragmentInteractionListener {
        //  Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/





}
