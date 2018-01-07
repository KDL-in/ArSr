package com.arsr.arsr.listener;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.arsr.arsr.costumview.AddTaskBottomDialog;

/**
 * 添加按钮监听器
 * Created by KundaLin on 18/1/5.
 */

public class OnFabClickListener implements View.OnClickListener {
    private static Context context;
    private AddTaskBottomDialog dialog;


    public OnFabClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        //dialog布局弹出
        dialog = new AddTaskBottomDialog();
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager());
    }
}
