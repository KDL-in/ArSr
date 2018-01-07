package com.arsr.arsr.util;

import android.content.Context;
import android.support.design.widget.BaseTransientBottomBar;
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
    public static void makeSnackbar(View v, String hint, final String undoStr, final OnSnackbarListener listener) {
        Snackbar.make(v,hint,Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击undo监听
                Toast.makeText(mContext, undoStr, Toast.LENGTH_SHORT).show();
                listener.onUndoClick();
            }
        }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {//无操作（确认）执行

            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                listener.onDismissed(event);
            }
        }).show();
    }

    public static void makeToast(String s) {
        Toast.makeText(mContext,s,Toast.LENGTH_SHORT).show();
    }

    public interface OnSnackbarListener {
        void onUndoClick();

        void onDismissed(int event);
    }

}
