package com.arsr.arsr.listener;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.arsr.arsr.MyApplication;
import com.arsr.arsr.activity.MainActivity;
import com.arsr.arsr.costumview.ShareBottomDialog;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.ListUtil;
import com.arsr.arsr.util.LogUtil;

import java.util.List;

/**
 * 添加按钮监听器
 * Created by KundaLin on 18/1/5.
 */

public class OnFabClickListener implements View.OnClickListener {
    private static Context context;
    private ShareBottomDialog dialog;


    public OnFabClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        //dialog布局弹出
        dialog = new ShareBottomDialog();
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager());
    }
}
