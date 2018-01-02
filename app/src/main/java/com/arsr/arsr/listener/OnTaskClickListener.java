package com.arsr.arsr.listener;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.adapter.TaskListRecyclerViewAdapter;
import com.arsr.arsr.util.AnimationUtils;

import drawthink.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import drawthink.expandablerecyclerview.listener.OnRecyclerViewListener;

/**
 * Created by KundaLin on 18/1/2.
 */

public class OnTaskClickListener implements OnRecyclerViewListener.OnItemClickListener {
    TaskListRecyclerViewAdapter adapter;
    public OnTaskClickListener(TaskListRecyclerViewAdapter listAdapter) {
        adapter = listAdapter;
    }

    @Override
    public void onGroupItemClick(int position, int groupPosition, View view) {
        //icon expand
        ImageView expandImg = view.findViewById(R.id.img_taskList_groupImgButton);
        expandImg.setSelected(!expandImg.isSelected());
//        view.setVisibility(View.GONE);
    }

    @Override
    public void onChildItemClick(int position, int groupPosition, int childPosition, View view) {
        LinearLayout result = view.findViewById(R.id.linearLayout_tasklist_result);
        LinearLayout item = view.findViewById(R.id.linearLayout_tasklist_item);

        AnimationUtils.showAndHiddenAnimation(item, AnimationUtils.AnimationState.STATE_HIDDEN, 500);
        AnimationUtils.showAndHiddenAnimation(result, AnimationUtils.AnimationState.STATE_SHOW, 500);
        AnimationUtils.showAndHiddenAnimation(result, AnimationUtils.AnimationState.STATE_HIDDEN, 4000);
        AnimationUtils.showAndHiddenAnimation(item, AnimationUtils.AnimationState.STATE_SHOW, 5000);
//        AnimationUtils.showAndHiddenAnimation();
    }
}

