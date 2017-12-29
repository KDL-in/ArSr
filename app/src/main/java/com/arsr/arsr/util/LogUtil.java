package com.arsr.arsr.util;

import android.util.Log;

/**
 * Created by KundaLin on 17/12/20.
 */

public class LogUtil {

    public static final int VERBOSE = 0;
    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WARN = 3;
    public static final int ERROR = 4;
    public static final int NOTING = 5;
    private static final String TAG = "default_test";
    public static int level = VERBOSE;

    public static void v(String tag, String msg) {
        if (level <= VERBOSE) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (level <= DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (level <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (level <= WARN) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (level <= ERROR) {
            Log.i(tag, msg);
        }
    }

    public static void d() {
        if (level <= DEBUG) {
            Log.d(TAG, "i am here");
        }
    }

    public static void d(String path) {
        if (level < DEBUG){
            Log.d(TAG, path);
        }
    }
}
