package com.arsr.arsr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.arsr.arsr.R;
import com.arsr.arsr.adapter.TasksInCalendarRecyclerViewAdapter;
import com.arsr.arsr.db.Task;
import com.arsr.arsr.fragment.FragmentTaskList;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.DateUtil;
import com.arsr.arsr.util.DrawableUtil;
import com.arsr.arsr.util.IOUtil;
import com.arsr.arsr.util.ListUtil;
import com.arsr.arsr.util.LogUtil;
import com.arsr.arsr.util.ToastUtil;
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

import static com.arsr.mexpandablerecyclerview.holder.BaseViewHolder.VIEW_TYPE_CHILD;

/**
 * 按日历查看任务列表
 * todo 任务推迟和提前 待做
 */
public class CalendarTaskActivity extends BasicActivity {

    private MaterialCalendarView calendarView;

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
        calendarView = findViewById(R.id.calendarView_week);
        CalendarDay today = DateUtil.getCalendarDayToday();
        //-设置范围(前后十五天）
        showListAt(DateUtil.getCalendarDayToday());
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
        calendarView.setCurrentDate(today, true);


    }

    private Map<String, Task> isSwipedable;

    /**
     * 初始化前后15天的任务数据
     */
    private void initWholeMonthData() {
        dateToTasks = new HashMap<>();
        //初始化过去15天
        initPastData();
        //初始化今天
        dateToTasks.put(DateUtil.getDateStringAfterToday(0), DBUtil.taskDAO.getTodayList());
        List<Task> today = dateToTasks.get(DateUtil.getDateStringAfterToday(0));
        isSwipedable = new HashMap<>();
        for (Task t :
                today) {
            isSwipedable.put(t.getName(), t);
        }
        //初始化未来15天
        initFutureData();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
//        super.onBackPressed();
    }

    private Map<String, List<Task>> dateToTasks;

    private void initFutureData() {
        List<Task> list = DBUtil.taskDAO.getList();
        for (Task t :
                list) {
            if (t == null) continue;
            int rPit[], aPit[] = IOUtil.getPointsInTimeOfAssist();
            rPit = IOUtil.getPointsInTimeOf(t.getTid());
            //recall
            List<Integer> nts = new ArrayList<>();
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
                if (cur == 0 && nts.get(0) != 0) nts.add(cur);
                for (int i = t.getAssistTimes(); i < aPit.length; i++) {
                    cur += aPit[i];
                    if (cur < flag) nts.add(cur);
                }
            }

            for (int i : nts) {
                if (i == 0) continue;
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

    private void initPastData() {
        for (int i = 1; i <= 15; i++) {
            String date = DateUtil.getDateStringBeforeToday(i);
            List<Task> pastList = IOUtil.getPastRecallRecord(date);
            dateToTasks.put(date, pastList);
        }
    }
    private TasksInCalendarRecyclerViewAdapter adapter;

    /**
     * 显示某一天的列表
     *
     * @param day
     */
    private void showListAt(CalendarDay day) {
        String date = DateUtil.dateToString(day.getDate());
        adapter = ListUtil.getListAdapterWith(CalendarTaskActivity.this, dateToTasks.get(date));
        FragmentTaskList fragment = FragmentTaskList.newInstance(adapter);
        //滑动推迟
        fragment.setTouchItemCallback(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                TasksInCalendarRecyclerViewAdapter.ViewHolder holder = (TasksInCalendarRecyclerViewAdapter.ViewHolder) viewHolder;

                if (holder.getItemViewType() != VIEW_TYPE_CHILD) return;
                String taskName = holder.getRealTaskName();
                if (isSwipedable.containsKey(taskName)) {
                    Task task = isSwipedable.get(taskName);
                    CalendarDay selDay = calendarView.getSelectedDate();
                    CalendarDay curDay = DateUtil.getCalendarDayToday();
                    int n = DateUtil.countDaysBetween(curDay, selDay);
                    if (task.getDayToRecall() == n || task.getDayToAssist() == n) {
                        if (direction == ItemTouchHelper.RIGHT && selDay.isBefore(calendarView.getMaximumDate())) {
                            if (moveTo(task, n, 1)) {//更新前后30天缓存
                                task.deferOneDay(n);
                                ToastUtil.makeToast("推迟一天");
                                DBUtil.taskDAO.update(task);//更新数据库
                                adapter.removeChild(holder.getAdapterPosition());
                            }

                        } else if (direction == ItemTouchHelper.LEFT && selDay.isAfter(curDay)) {
                            if (moveTo(task, n, -1)) {
                                task.aheadOfOneDay(n);
                                ToastUtil.makeToast("提前一天");
                                DBUtil.taskDAO.update(task);//更新数据库
                                adapter.removeChild(holder.getAdapterPosition());
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

        });


        loadFragment(fragment);
/**/

    }

    /**
     * 移动任务到临近日，成功返回true失败返回false
     */
    private boolean moveTo(Task task, int n, int i) {
        String cur = DateUtil.getDateStringAfterToday(n);
        String next = DateUtil.getDateStringAfterToday(i+n);
        List<Task> curList = dateToTasks.get(cur);
        List<Task> nextList = dateToTasks.get(next);
        if (nextList==null){//如果为空
            nextList = new ArrayList<>();
            dateToTasks.put(next, nextList);
        }
        if (nextList.contains(task)){
            ToastUtil.makeToast("距离该任务下一次执行时间为1");
            return false;
        }
        nextList.add(task);
        curList.remove(task);
        return true;
    }


    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout_tadUI__replace, fragment);
        transaction.commit();
    }

    public static void startAction(AppCompatActivity context) {
        Intent intent = new Intent(context, CalendarTaskActivity.class);
        context.startActivityForResult(intent,1);
    }

    /**
     * 当前日期的装饰
     */
    private class TodaySelectorDecorator implements DayViewDecorator {
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.equals(DateUtil.getCalendarDayToday());
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(7, DrawableUtil.getColor(CalendarTaskActivity.this, R.color.colorAccent)));
        }
    }
}
