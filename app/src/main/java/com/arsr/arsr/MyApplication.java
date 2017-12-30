package com.arsr.arsr;

import android.app.Application;
import android.content.Context;


/**
 * Created by KundaLin on 17/12/29.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
