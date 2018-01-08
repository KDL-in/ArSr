package com.arsr.arsr.listener;

import android.support.annotation.NonNull;

import com.arsr.arsr.activity.TaskActivity;
import com.arsr.arsr.util.DateUtil;
import com.arsr.arsr.util.ToastUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

/**
 * 任务界面，日历中日期点击修改监听
 * todo 时间修改待完善
 * 时间修改还是比较复杂的，要解决今日任务和下一次任务的区分，还有assist和recall的区分
 * Created by KundaLin on 18/1/8.
 */

public class OnDateDouSelectChangedListener implements OnDateSelectedListener {
    public OnDateDouSelectChangedListener(TaskActivity context) {
    }

    @Override
    public void onDateSelected(@NonNull final MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        if (!date.isAfter(CalendarDay.today())) {
            TaskActivity.lastDay = date;
            return;//跳过今天
        }
        if (TaskActivity.lastDay!=null&&TaskActivity.lastDay.equals(date)) {//第二次选择
            ToastUtil.makeSnackbar(widget, "确认修改下一次时间？", "取消修改", new ToastUtil.OnSnackbarListener() {
                @Override
                public void onUndoClick() {

                }

                @Override
                public void onDismissed(int event) {
                    ToastUtil.makeToast("时间修改待完善");
//                    widget.refreshDrawableState();
                }

                @Override
                public void onShown() {
                }
            });
            TaskActivity.lastDay = null;
        } else {
            ToastUtil.makeToast("再次点击修改下次执行时间");
            TaskActivity.lastDay = date;
        }
    }
}
