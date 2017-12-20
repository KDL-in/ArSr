package com.arsr.arsr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.arsr.arsr.util.LogUtil;

/**
 * Created by KundaLin on 17/12/20.
 */

public class BasicActivity extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.v("CurrentActivity",getClass().getSimpleName());
        ActivityCollector.addActivitiy(this);
    }

}
