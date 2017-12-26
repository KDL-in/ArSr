package com.arsr.arsr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arsr.arsr.R;
import com.arsr.arsr.adapter.AdapterCategoryListRecyclerView;
import com.arsr.arsr.entity.Child;
import com.arsr.arsr.entity.Group;

import java.util.List;

import drawthink.expandablerecyclerview.bean.RecyclerViewData;


/**
 * 类别碎片
 * 等待加入内容
 * todo 等待抽象
 */
public class FragmentCategoryList extends Fragment {
    // 数据
    private static List<RecyclerViewData> categoryListData;


    public FragmentCategoryList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param groups  列表中组的数据
     * @param children  列表中子项的数据.
     * @return A new instance of fragment FragmentTaskList.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCategoryList newInstance(List<Group>groups, List<List<Child>>children) {
        FragmentCategoryList fragment = new FragmentCategoryList();
        categoryListData = AdapterCategoryListRecyclerView.initExListData(groups,children);
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
        AdapterCategoryListRecyclerView listAdapter = new AdapterCategoryListRecyclerView(getActivity(), categoryListData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerCategoryList.setAdapter(listAdapter);
        recyclerCategoryList.setLayoutManager(layoutManager);
        return view;
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
