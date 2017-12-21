package com.arsr.arsr.costumview;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.entity.Task;

/**
 * Created by KundaLin on 17/12/20.
 * 单个任务的自定义控件
 * todo 后期需要添加对各种属性操作的接口
 */

public class TaskLayout extends CardView {
    private TextView nameTextView;//任务信息：任务名
    public TaskLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_task, this);
        nameTextView = findViewById(R.id.text_task_name);
    }

    /**
     * 设置task的属性
     * @param task
     */
    public void setTaskProperties(Task task) {
        nameTextView.setText(task.getName());//任务名设置
    }
}
