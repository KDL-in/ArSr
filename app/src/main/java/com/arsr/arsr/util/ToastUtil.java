package com.arsr.arsr.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.arsr.arsr.MyApplication;

/**
 * Created by KundaLin on 18/1/6.
 */

public class ToastUtil {
    private static Context mContext;
    static {
        mContext = MyApplication.getContext();
    }
    public static void makeSnackbar(View v, String doStr, final String undoStr, final OnSnackbarUndoListener listener) {
        Snackbar.make(v,doStr,Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, undoStr, Toast.LENGTH_SHORT).show();
                listener.run();
            }
        }).show();
    }

    public static void makeToast(String s) {
        Toast.makeText(mContext,s,Toast.LENGTH_SHORT).show();
    }

    public interface OnSnackbarUndoListener{
        void run();
    }

}
