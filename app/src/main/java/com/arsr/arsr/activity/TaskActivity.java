package com.arsr.arsr.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.arsr.arsr.MyApplication;
import com.arsr.arsr.R;
import com.arsr.arsr.db.Task;
import com.arsr.arsr.listener.OnDateDouSelectChangedListener;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.DateUtil;
import com.arsr.arsr.util.DrawableUtil;
import com.arsr.arsr.util.IOUtil;
import com.arsr.arsr.util.LogUtil;
import com.arsr.arsr.util.ToastUtil;
import com.arsr.arsr.util.UIDataUtil;
import com.bumptech.glide.Glide;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;


/**
 * 任务界面活动
 */
public class TaskActivity extends BasicActivity {
    public static CalendarDay lastDay = null;
    private MaterialCalendarView calendarView;
    //测试数据
    private String datesOfRecall[];
    private String datesOfAssist[];
    private HashSet<CalendarDay> rSets;
    private HashSet<CalendarDay> aSets;
    private  int nextDay = 0;
    private ImageView bgImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获得数据
        Intent intent = getIntent();
        final String name = intent.getStringExtra("taskName");

        setContentView(R.layout.activity_task);
        //状态栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {//返回按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //设置task名
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle(DBUtil.getSubstringName(name, '_').replaceAll("_", " "));
        //设置图片
        bgImg = findViewById(R.id.img_taskUI_bg);
        DrawableUtil.setImg(this,bgImg);

        //日历相关
        calendarView = findViewById(R.id.calendarView_month);
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
//        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_DEFAULTS);
        initDatas(name);
        //-设置范围
        Calendar minCal = DateUtil.getCalendar();
        minCal.add(Calendar.MONTH, -2);
        Calendar maxCal = Calendar.getInstance();
        maxCal.add(Calendar.MONTH, 1);
        calendarView.state().edit().setMinimumDate(minCal).setMaximumDate(maxCal).commit();
        calendarView.setSelectedDate(DateUtil.getCalendar().getTime());
        //-添加装饰
        calendarView.addDecorators(new PastDaySelectorDecorator(),new NextAssistDecorator(),new RecallDayDecorator(),new TodaySelectorDecorator(),new NextRecallDecorator());
        //-监听
        calendarView.setOnDateChangedListener(new OnDateDouSelectChangedListener(this));
        //浮动按钮的监听
        FloatingActionButton button = findViewById(R.id.fab_taskUI);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.makeSnackbar(v, "是否删除", "撤销删除", new ToastUtil.OnSnackbarListener() {
                    @Override
                    public void onUndoClick() {

                    }

                    @Override
                    public void onDismissed(int event) {
                        if (event != BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_ACTION) {
                            DBUtil.taskDAO.delete(name);
                            UIDataUtil.updateUIData(UIDataUtil.TYPE_TASK_CHANGED);
                            finish();
                        }
                    }

                    @Override
                    public void onShown() {
                    }
                });
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initDatas(String name) {
        //取出recall datesOfRecall
        datesOfRecall = IOUtil.getRecallDatesOf(name);
        rSets = new HashSet<>();
        for (int i = 0; i < datesOfRecall.length; i++) {//转换数据
            if (datesOfRecall[i].equals(""))continue;
            Date date = DateUtil.stringToDate(datesOfRecall[i]);
            CalendarDay day = CalendarDay.from(date);
            rSets.add(day);
        }
        Task task = DBUtil.taskDAO.get(name);
        if (task.getDayToRecall() == -1) {
            int timeInPoint[] = IOUtil.getPointsInTimeOf(DBUtil.tagDAO.get(task.getTid()));
            nextDay = timeInPoint[task.getTimes()];
        } else {
            nextDay = task.getDayToRecall();
        }
        //assist datas
        datesOfAssist = IOUtil.getAssistDatesOf(name);
        LogUtil.d(datesOfAssist.length+"");
        aSets = new HashSet<>();
        for (int i = 0; i < datesOfAssist.length; i++) {//转换数据
            if (datesOfAssist[i].equals(""))continue;
            Date date = DateUtil.stringToDate(datesOfAssist[i]);
            CalendarDay day = CalendarDay.from(date);
            aSets.add(day);
        }

    }

    /**
     * 启动方法
     */
    public static void actionStart(String name) {
        Context context = MyApplication.getContext();
        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra("taskName", name);
        context.startActivity(intent);
    }

    /**
     * recall datesOfRecall 的装饰
     */
    private class RecallDayDecorator implements DayViewDecorator {

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return rSets.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            Drawable drawable = DrawableUtil.getDrawable(TaskActivity.this, R.drawable.calendar_recallmark);
            DrawableUtil.tintDrawable(TaskActivity.this, drawable, R.color.colorPrimary);
            view.setBackgroundDrawable(drawable);
            view.setDaysDisabled(true);
        }
    }
    /**
     * assist日期的装饰
     */
    private class NextAssistDecorator implements DayViewDecorator {
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return aSets.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            Drawable drawable = DrawableUtil.getDrawable(TaskActivity.this, R.drawable.calendar_assistmark);
            DrawableUtil.tintDrawable(TaskActivity.this, drawable, R.color.colorAssist);
            view.setBackgroundDrawable(drawable);
            view.setDaysDisabled(true);
        }
    }
    private CalendarDay today = DateUtil.getCalendarDayToday();//今天
    /**
     * 禁止过去的选择
     */
    private class PastDaySelectorDecorator implements DayViewDecorator {

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.isBefore(today);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
        }
    }

    /**
     * 当前日期的装饰
     */
    private class TodaySelectorDecorator implements DayViewDecorator {
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.equals(today);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(7, getResources().getColor(R.color.colorAccent)));
        }
    }

    /**
     * 下一次执行的装饰
     */
    private class NextRecallDecorator implements DayViewDecorator {
        CalendarDay day;

        public NextRecallDecorator() {
            Calendar calendar = DateUtil.getCalendar();
            calendar.add(Calendar.DAY_OF_MONTH, nextDay);
            day = CalendarDay.from(calendar);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return this.day.equals(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_calendar_nextday));
        }
    }


}
