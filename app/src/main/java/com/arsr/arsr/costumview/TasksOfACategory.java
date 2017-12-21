package com.arsr.arsr.costumview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.entity.Task;
import com.arsr.arsr.util.LogUtil;

import java.util.List;

/**
 * Created by KundaLin on 17/12/20.
 * 某类别的所有任务的自定义控件，可以展示类别下所有任务
 * 1.控制显示下拉任务的监听
 * 2.todo 动态添加task项，后期需要改成接口形式
 */

public class TasksOfACategory extends LinearLayout implements View.OnClickListener {
    private Context context;//构造函数中的参数，用于动态添加子任务
    private AttributeSet attrs;//同上
    private LinearLayout tasksLayout;
    private ImageView imgExpand;
    private boolean isHidden = false;
    public TasksOfACategory(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_tasks_of_category, this);
        init();
        this.context = context;
        this.attrs = attrs;
        imgExpand.setOnClickListener(this);

        //todo 动态添加task项 后期需要改成接口形式
//        tasksLayout.addView(new TaskLayout(context, attrs));
//        tasksLayout.addView(new TaskLayout(context, attrs));
//        tasksLayout.addView(new TaskLayout(context, attrs));
//        TextView textView = tasksLayout.getChildAt(1).findViewById(R.id.text_task_name);
//        textView.setText("ceshi");测试自定义值
    }

    private void init() {
        tasksLayout = findViewById(R.id.layout_tasks);
        imgExpand = findViewById(R.id.img_expand);
    }

    @Override
    public void onClick(View view) {
        imgExpand.setSelected(!isHidden);
        if (isHidden) {
            tasksLayout.setVisibility(LinearLayout.VISIBLE);
        } else {
            tasksLayout.setVisibility(LinearLayout.GONE);
        }
        isHidden = !isHidden;
    }

    /**
     * 将子任务动态添加进本类别中
     * @param tasks 传入类别的子任务集
     */
    public void addTasks(List<Task> tasks) {
        for (Task task :
                tasks) {
            TaskLayout taskLayout = new TaskLayout(context, attrs);
            taskLayout.setTaskProperties(task);
            tasksLayout.addView(taskLayout);
        }
    }
}
