package com.arsr.arsr.listener;

import android.view.View;
import android.widget.ImageView;

import com.arsr.arsr.R;

import drawthink.expandablerecyclerview.listener.OnRecyclerViewListener;

/**
 * Created by KundaLin on 18/1/2.
 */

public class OnTaskClickListener implements OnRecyclerViewListener.OnItemClickListener {
    @Override
    public void onGroupItemClick(int position, int groupPosition, View view) {
        //icon expand
        ImageView expandImg = view.findViewById(R.id.img_taskList_groupImgButton);
        expandImg.setSelected(!expandImg.isSelected());
    }

    @Override
    public void onChildItemClick(int position, int groupPosition, int childPosition, View view) {

    }
}

