package com.arsr.arsr.costumview;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.arsr.arsr.R;

/**
 * Created by KundaLin on 17/12/20.
 * 单个任务的自定义控件
 * todo 后期需要添加对各种属性操作的接口
 */

public class TaskLayout extends CardView {
    public TaskLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_task, this);
    }

}
