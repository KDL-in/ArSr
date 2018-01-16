package com.arsr.arsr.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.arsr.arsr.R;
import com.arsr.arsr.util.DrawableUtil;
import com.arsr.arsr.util.LogUtil;

/**
 * Created by KundaLin on 17/12/20.
 * 所有活动的父类
 * 1. 统一打印活动
 * 2. 统一调用ActivityCollector进行管理
 */

public class BasicActivity extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(DrawableUtil.themeId);
        super.onCreate(savedInstanceState);;
        LogUtil.v("CurrentActivity",getClass().getSimpleName());
        ActivityCollector.addActivity(this);
    }

}
