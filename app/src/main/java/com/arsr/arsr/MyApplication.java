package com.arsr.arsr;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by KundaLin on 17/12/29.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
    }

    public static Context getContext() {
        return context;
    }
}
