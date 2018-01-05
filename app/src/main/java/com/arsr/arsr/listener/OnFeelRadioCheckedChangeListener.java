package com.arsr.arsr.listener;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.adapter.TaskListRecyclerViewAdapter;
import com.arsr.arsr.db.Tag;
import com.arsr.arsr.db.Task;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.IOUtil;


/**
 * Created by KundaLin on 18/1/4.
 */

public class OnFeelRadioCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
    private View view;
    private TaskListRecyclerViewAdapter adapter;

    public OnFeelRadioCheckedChangeListener(TaskListRecyclerViewAdapter adapter, View view) {
        this.adapter = adapter;
        this.view = view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //设置完成标志
        view.setTag(true);
        TextView nameTv = view.findViewById(R.id.tv_taskList_childTittle);//颜色改变
        ImageView taskIco = view.findViewById(R.id.img_taskList_icon);
        nameTv.setSelected(true);
        taskIco.setSelected(true);//颜色改变end
        switch (checkedId) {
            case R.id.rdiobtn_tasklist_feelbadman:
                TextView categoryTv = view.findViewById(R.id.tv_taskList_childCategory);
                Task task = DBUtil.getTask(DBUtil.packing(categoryTv.getText() + " " + nameTv.getText()));
                Tag tag = DBUtil.getTag(task.getTid());
                IOUtil.adjustPointInTimeOf(tag, task, IOUtil.FLAG_FEEL_BAD);//微调
                //辅助recall唤醒
                if (task.getTimes() >= 2) {
                    task.setDayToAssist(-1);
                    task.setAssistTimes(0);
                    DBUtil.updateTask(task);
                }

                break;
            case R.id.rdiobtn_tasklist_feelgoodman:
                categoryTv = view.findViewById(R.id.tv_taskList_childCategory);
                task = DBUtil.getTask(DBUtil.packing(categoryTv.getText() + " " + nameTv.getText()));
                tag = DBUtil.getTag(task.getTid());
                IOUtil.adjustPointInTimeOf(tag, task, IOUtil.FLAG_FEEL_GOOD);
                break;
            default:
                break;
        }
    }
}
