package com.arsr.arsr.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.arsr.arsr.MyApplication;
import com.arsr.arsr.R;
import com.arsr.arsr.activity.SettingActivity;
import com.arsr.arsr.util.IOUtil;
import com.arsr.arsr.util.LogUtil;
import com.arsr.arsr.util.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebugFragment extends Fragment {
    private  AppCompatActivity context;
    private CheckBox debugCheckBox;
    private Button showLogBtn, clearLogBtn;
    private TextView logTv;

    public DebugFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public DebugFragment(AppCompatActivity context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_debug, container, false);
        init(view);
        //输出日志
        debugCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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

        return view;
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
    private void init(View view) {
        debugCheckBox = view.findViewById(R.id.checkbox_setting_debug);
        showLogBtn = view.findViewById(R.id.btn_setting_showLog);
        logTv = view.findViewById(R.id.tv_setting_showLog);
        clearLogBtn = view.findViewById(R.id.btn_setting_clear);
        debugCheckBox.setChecked(IOUtil.ifOutputLog);
    }

}
