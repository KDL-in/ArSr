package com.arsr.arsr.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.util.IOUtil;
import com.arsr.arsr.util.LogUtil;
import com.arsr.arsr.util.ToastUtil;

public class SettingActivity extends BasicActivity {
    private CheckBox debugCheckBox;
    private Button showLogBtn,clearLogBtn;
    private TextView logTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        //输出日志
        debugCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    checkDebugBtn();
                }

            }
        });
        showLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (debugCheckBox.isChecked()) {
                    String str = IOUtil.readLogFromFile();
                    logTv.setText(str);
                } else {
                    logTv.setText("");
                }
            }
        });
        clearLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (debugCheckBox.isChecked()) {
                    logTv.setText("");
                    IOUtil.clearLog();
                }
            }
        });
    }

    private void checkDebugBtn() {
        if (debugCheckBox.isChecked()) {
            IOUtil.ifOutputLog = true;
            LogUtil.level = LogUtil.TOFILE;
        } else {
            IOUtil.ifOutputLog = false;
            LogUtil.level = LogUtil.VERBOSE;
        }
    }

    private void init() {
        debugCheckBox = findViewById(R.id.checkbox_setting_debug);
        showLogBtn = findViewById(R.id.btn_setting_showLog);
        logTv = findViewById(R.id.tv_setting_showLog);
        clearLogBtn = findViewById(R.id.btn_setting_clear);
        debugCheckBox.setChecked(IOUtil.ifOutputLog);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkDebugBtn();
                } else {
                    ToastUtil.makeToast("失败，没有权限");
                    debugCheckBox.setChecked(false);
                }
                break;
        }
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }
}
