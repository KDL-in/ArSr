package com.arsr.arsr.costumview;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.arsr.arsr.R;

/**
 * Created by KundaLin on 17/12/20.
 * 单个任务的自定义控件
 */

public class TaskLayout extends CardView {
    private TextView nameTextView;//任务信息：任务名
    public TaskLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_tasklist_child, this);
        nameTextView = findViewById(R.id.tv_taskList_groupTitle);
    }

}
