package com.arsr.arsr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.arsr.arsr.R;
import com.arsr.arsr.adapter.TaskListRecyclerViewAdapter;
import com.arsr.arsr.db.Task;
import com.arsr.arsr.db.dao.DAO;
import com.arsr.arsr.fragment.FragmentTaskList;
import com.arsr.arsr.listener.OnTaskListLongClickListener;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.DateUtil;
import com.arsr.arsr.util.DrawableUtil;
import com.arsr.arsr.util.IOUtil;
import com.arsr.arsr.util.ListUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 按日历查看任务列表
 */
public class CalendarTaskActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendarlist);
        //显示列表
        initWholeMonthData();
        //状态栏更换
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {//返回按钮
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
        //标题取消
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitleEnabled(false);
        //日历相关
        MaterialCalendarView calendarView = findViewById(R.id.calendarView_week);
        CalendarDay today = CalendarDay.today();
        //-设置范围(前后十五天）
        showListAt(CalendarDay.today());
        Calendar maxCal = Calendar.getInstance();
        maxCal.add(Calendar.DAY_OF_MONTH, 15);
        Calendar minCal = Calendar.getInstance();
        minCal.add(Calendar.DAY_OF_MONTH, -15);
        calendarView.state().edit().setMaximumDate(maxCal).setMinimumDate(minCal).commit();
        calendarView.addDecorator(new TodaySelectorDecorator());
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                showListAt(date);
            }
        });
        calendarView.setDateSelected(today, true);


    }

    /**
     * 初始化前后15天的任务数据
     */
    private void initWholeMonthData() {
        dateToTasks = new HashMap<>();
        //初始化过去15天
        initPastData();
        //初始化今天
        dateToTasks.put(DateUtil.getDateStringAfterToday(0), DBUtil.taskDAO.getTodayList());
        //初始化未来15天
        initFutureData();

    }

    private Map<String, List<Task>> dateToTasks;
    private void initFutureData(){
        List<Task> list = DBUtil.taskDAO.getList();
        for (Task t :
                list) {
            if (t == null) continue;
            Map<Long,int[]>pit = new HashMap<>();//时间点缓存
            int rPit[],aPit[]= IOUtil.getPointsInTimeOfAssist();
            if (pit.containsKey(t.getTid())) {
                rPit = pit.get(t.getTid());
            } else {
                rPit = IOUtil.getPointsInTimeOf(DBUtil.tagDAO.get(t.getTid()));
                pit.put(t.getTid(), rPit);
            }
            //recall
            List<Integer>nts =new ArrayList<>();
            int cur = t.getDayToRecall() == -1 ? 0 : t.getDayToRecall();
            nts.add(cur);
            for (int i = t.getTimes(); i < rPit.length; i++) {
                cur += rPit[i];
                nts.add(cur);
            }
            //assist
            if (t.getDayToAssist() != -2) {
                int flag = nts.get(0) == 0 ? nts.get(1) : nts.get(0);
                cur = t.getDayToAssist() == -1 ? 0 : t.getDayToAssist();
                if (cur==0&&nts.get(0)!=0)nts.add(cur);
                for (int i = t.getAssistTimes(); i <aPit.length ; i++) {
                    cur += aPit[i];
                    if (cur<flag) nts.add(cur);
                }
            }

            for (int i : nts) {
                if (i==0)continue;
                String date = DateUtil.getDateStringAfterToday(i);
                if (dateToTasks.containsKey(date)) {
                    List<Task> tasksOfDate = dateToTasks.get(date);
                    tasksOfDate.add(t);
                } else {
                    List<Task> tasksOfDate = new ArrayList<>();
                    tasksOfDate.add(t);
                    dateToTasks.put(date, tasksOfDate);
                }
            }
        }
    }
    /*private void initFutureData() {
        List<Task> list = DBUtil.taskDAO.getList();
        Map<String,List<String>>dateToTasks = new HashMap<>();
        for (Task t :
                list) {
            if (t == null) continue;
            Map<Long,int[]>pit = new HashMap<>();//时间点缓存
            int rPit[],aPit[]= IOUtil.getPointsInTimeOfAssist();
            if (pit.containsKey(t.getTid())) {
                rPit = pit.get(t.getTid());
            } else {
                rPit = IOUtil.getPointsInTimeOf(DBUtil.tagDAO.get(t.getTid()));
                pit.put(t.getTid(), rPit);
            }
            int times = t.getTimes();//第几次recall
            int aTimes = t.getAssistTimes();
            int lastA = 0, lastR = 0, lastT = 0;
            boolean flag = t.getDayToAssist() != -2;
            while (times <= 6) {//计算任务之后每一次的执行时间
                //获取下一次执行时间nt
                //-获得下一次recall时间rt
                int rt,at=0,nt;
                if (times == t.getTimes()) {//当前次数
                    rt = t.getDayToRecall() == -1 ? 0 : t.getDayToRecall();
                } else {
                    rt = rPit[times-1] + lastR;
                }
                //-获得下一次assist时间at
                if (flag) {
                    if (aTimes == t.getAssistTimes()) {
                        at = t.getDayToAssist() == -1 ? 0 : t.getDayToAssist();
                    } else {
                        at = aPit[aTimes-1] + lastA;
                    }
                }
                if (flag&&at < rt) {//取近的
                    nt = at;
                    lastA += at;
                    aTimes++;
                } else {
                    nt = rt;
                    times++;
                    lastR += rt;
                    flag = false;
                }
                String date = DateUtil.getDateStringAfterToday(nt);
                if (dateToTasks.containsKey(date)) {
                    List<String> tasksOfDate = dateToTasks.get(date);
                    tasksOfDate.add(t.getName());
                } else {
                    List<String> tasksOfDate = new ArrayList<>();
                    tasksOfDate.add(t.getName());
                    dateToTasks.put(date, tasksOfDate);
                }
            }
        }
        //测试打印
        for (String key :
                dateToTasks.keySet()) {
            List<String> list1 = dateToTasks.get(key);
            LogUtil.d(key+"日需要执行"+list1.size());
            for (String name :
                    list1) {
                LogUtil.d(name);
            }
        }
    }*/

    private void initPastData() {
        for (int i = 1; i <= 15; i++) {
            String date = DateUtil.getDateStringAfterToday(-i);
            IOUtil.getPastRecallRecord(date);
        }
    }

    /**
     * 显示某一天的列表
     * @param day
     */
    private void showListAt(CalendarDay day) {
        String date = DateUtil.dateToString(day.getDate());
        TaskListRecyclerViewAdapter adapter = ListUtil.getListAdapterWith(CalendarTaskActivity.this,dateToTasks.get(date));
        FragmentTaskList fragment = FragmentTaskList.newInstance(adapter);
        if (day.equals(CalendarDay.today())) {
            //添加监听
            fragment.setOnItemLongClickListener(new OnTaskListLongClickListener(adapter));
        }
        loadFragment(fragment);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout_tadUI__replace, fragment);
        transaction.commit();
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, CalendarTaskActivity.class);
        context.startActivity(intent);
    }
    /**
     * 当前日期的装饰
     */
    private class TodaySelectorDecorator implements DayViewDecorator {
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.equals(CalendarDay.today());
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(7,DrawableUtil.getColor(CalendarTaskActivity.this,R.color.colorAccent)));
        }
    }
}
