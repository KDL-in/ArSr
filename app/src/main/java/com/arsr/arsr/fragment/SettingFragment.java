package com.arsr.arsr.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.activity.ActivityCollector;
import com.arsr.arsr.activity.SettingActivity;
import com.arsr.arsr.util.DrawableUtil;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    private AppCompatActivity context;
    private CircleImageView blueBtn;
    private CircleImageView redBtn;
    private Button debugBtn;
    public SettingFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public SettingFragment(AppCompatActivity context) {
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        init(view);
        blueBtn.setOnClickListener(this);
        redBtn.setOnClickListener(this);
        debugBtn.setOnClickListener(this);
        return view;
    }

    private void init(View view) {
        blueBtn = view.findViewById(R.id.imgBtn_blue);
        redBtn = view.findViewById(R.id.imgBtn_red);
        debugBtn = view.findViewById(R.id.btn_setting_debug);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_blue:
                DrawableUtil.setTheme(R.style.AppTheme);
                ActivityCollector.finishAll();
                break;
            case R.id.imgBtn_red:
                DrawableUtil.setTheme(R.style.RedTheme);
                ActivityCollector.finishAll();
                break;
            case R.id.btn_setting_debug:
                ((SettingActivity) context).showLog();
        }
    }
}
