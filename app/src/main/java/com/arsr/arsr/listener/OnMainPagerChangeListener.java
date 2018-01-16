package com.arsr.arsr.listener;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import com.arsr.arsr.MyApplication;
import com.arsr.arsr.R;
import com.arsr.arsr.util.DrawableUtil;

/**
 * viewpager的滑动监听，标题改变，颜色渐变等等
 * 1、onPageScrollStateChanged(int state) 对手指监听，按下(1)，滑动（2），抬起（0）
 * 2、onPageScrolled(int position, float positionOffset, int posiotionOffsetPixels)
 * 在屏幕滑动中不断调用，position当前滑动到那个页面，positionOffset滑动比例，
 * posiotionOffsetPixels滑动像素
 * 3、onPageSelected(int position) 代表已经滑动到那个页面了
 * Created by KundaLin on 18/1/5.
 */

public class OnMainPagerChangeListener implements ViewPager.OnPageChangeListener {
    private Context context;
    private ViewPager viewPager;
    private AppBarLayout appBarLayout;

    private int PAGE_COLOR_ONE;
    //    private final int PAGE_COLOR_ONE = ContextCompat.getColor(MyApplication.getContext(), R.attr.colorPrimary);
    private int PAGE_COLOR_TWO;
    //    private final int PAGE_COLOR_TWO = ContextCompat.getColor(MyApplication.getContext(), R.color.colorAssist);
    private Window window;

    public OnMainPagerChangeListener(ViewPager viewPager, Context context) {
        this.context = context;
        this.viewPager = viewPager;
        appBarLayout = ((AppCompatActivity) context).findViewById(R.id.appBarLayout_mainUI);
        window = ((AppCompatActivity) context).getWindow();
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //getColorFromAttr
        PAGE_COLOR_ONE = DrawableUtil.getColorFromAttr(context,R.attr.colorPrimary);
        PAGE_COLOR_TWO = DrawableUtil.getColorFromAttr(context, R.attr.colorAssist);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        ArgbEvaluator evaluator = new ArgbEvaluator(); // ARGB求值器
        int evaluate; // 初始默认颜色（透明白）
        if (position == 0) {
            evaluate = (Integer) evaluator.evaluate(positionOffset, PAGE_COLOR_ONE, PAGE_COLOR_TWO); // 根据positionOffset和第0页~第1页的颜色转换范围取颜色值
        } else {
            evaluate = PAGE_COLOR_TWO;
        }
        appBarLayout.setBackgroundColor(evaluate); // 为ViewPager的父容器设置背景色
        //设置状态栏颜色
        window.setStatusBarColor(evaluate);

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                ((AppCompatActivity) context).getSupportActionBar().setTitle("今日任务");
                break;
            case 1:
                ((AppCompatActivity) context).getSupportActionBar().setTitle("分类");
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
