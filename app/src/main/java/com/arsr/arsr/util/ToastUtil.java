package com.arsr.arsr.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
        Snackbar.make(v, hint, Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击undo监听
                Toast.makeText(mContext, undoStr, Toast.LENGTH_SHORT).show();
                listener.onUndoClick();
            }
        }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {//无操作（确认）执行

            @Override
            public void onShown(Snackbar transientBottomBar) {
                listener.onShown();
            }

            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                listener.onDismissed(event);
            }
        }).show();
    }

    public static void makeToast(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }

    public static void makeDialog(Context context,String msg, final OnAlterDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("注意：");
        builder.setMessage(msg);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onPositiveButtonClick(dialog,which);
            }
        });
        builder.show();
    }

    public interface OnAlterDialogListener {
        void onPositiveButtonClick(DialogInterface dialog, int which);
    }

    /**
     * snackbar监听接口
     */
    public interface OnSnackbarListener {
        void onUndoClick();

        void onDismissed(int event);

        void onShown();
    }

}
