package com.arsr.arsr.listener;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.adapter.TaskListRecyclerViewAdapter;
import com.arsr.arsr.db.Tag;
import com.arsr.arsr.db.Task;
import com.arsr.arsr.util.AnimationUtils;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.IOUtil;

import drawthink.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import drawthink.expandablerecyclerview.listener.OnRecyclerViewListener;

/**
 * Created by KundaLin on 18/1/2.
 */

public class OnTaskListClickListener implements OnRecyclerViewListener.OnItemClickListener {
    TaskListRecyclerViewAdapter adapter;

    public OnTaskListClickListener(TaskListRecyclerViewAdapter listAdapter) {
        adapter = listAdapter;
    }

    @Override
    public void onGroupItemClick(int position, int groupPosition, View view) {
        //icon expand
        ImageView expandImg = view.findViewById(R.id.img_taskList_groupImgButton);
        boolean isSelected = (boolean) view.getTag();
        expandImg.setSelected(!isSelected);
        view.setTag(!isSelected);
//        view.setVisibility(View.GONE);
    }

    @Override
    public void onChildItemClick(int position, int groupPosition, int childPosition, View view) {
        Task task = adapter.getTask(groupPosition, childPosition);
        //显示效果选择按钮
        LinearLayout result = view.findViewById(R.id.linearLayout_tasklist_result);
        TextView nameTv = view.findViewById(R.id.tv_taskList_childTittle);//颜色改变
        ImageView taskIco = view.findViewById(R.id.img_taskList_icon);
        boolean isFinish = !(boolean) view.getTag();
        view.setTag(isFinish);
        nameTv.setSelected(isFinish);
        taskIco.setSelected(isFinish);//颜色改变end
        if (task.getDayToRecall()==0) {//辅助recall下不显示效果选择面板
            AnimationUtils.showAndHiddenAnimation(result, AnimationUtils.AnimationState.STATE_SHOW, 100);
            AnimationUtils.showAndHiddenAnimation(result, AnimationUtils.AnimationState.STATE_HIDDEN, 4000);
        }
        //操作取消（回滚）
        if (!isFinish){//取消完成
            result.clearAnimation();
            if (task.getDayToRecall()==-1) task.setDayToRecall(0);
            else if(task.getDayToAssist()==-1) task.setDayToAssist(0);
            IOUtil.cancelSaveRecallDate(task);//取消本地存储
            //取消微调
            Tag tag = DBUtil.getTag(task.getTid());
            IOUtil.cancelAdjustPointInTimeOf(tag, task);
            //取消辅助recall
            if (task.getAssistTimes() == 0) {
                task.setAssistTimes(-2);
                task.setDayToAssist(-2);
            }
            DBUtil.updateTask(task);//取消数据库
            return;
        }
        // 点击完成
        if (task.getDayToRecall() == 0) {//正常执行时间点
            task.setDayToRecall(-1);
            DBUtil.updateTask(task);//改数据库
            IOUtil.saveRecallDate(task);//改本地存储
            //todo 这个事做了之后，就应该把reset辅助位
        } else if (task.getDayToAssist() == 0) {//辅助执行时间点
            task.setDayToAssist(-1);
            DBUtil.updateTask(task);
            IOUtil.saveAssistDate(task);
        }
    }
}

