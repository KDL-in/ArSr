package com.arsr.arsr.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.fragment.DebugFragment;
import com.arsr.arsr.fragment.SettingFragment;
import com.arsr.arsr.util.DrawableUtil;
import com.arsr.arsr.util.IOUtil;
import com.arsr.arsr.util.LogUtil;
import com.arsr.arsr.util.ToastUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends BasicActivity {
    private static AppCompatActivity context;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        Fragment fragment = new SettingFragment(this);
        loadFragment(fragment,R.id.frameLayout_settingUI_replace);
        //设置状态栏颜色
        Window window = getWindow();
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(DrawableUtil.getColorFromAttr(this,R.attr.colorPrimary));
    }


    private void loadFragment(Fragment fragment,int replaceId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(replaceId, fragment);
        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LogUtil.d("request");
        switch (requestCode) {
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.makeToast("备份申请成功");
                    LogUtil.d("2");
                    IOUtil.backup();
                } else {
                    ToastUtil.makeToast("失败，没有权限");
                }
                break;

            case 3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.makeToast("还原申请成功");
                    IOUtil.recovery();
                } else {
                    ToastUtil.makeToast("失败，没有权限");
                }
                break;

        }
    }
    public static void startAction(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
        SettingActivity.context = (AppCompatActivity) context;
    }

    public void showLog() {
        Fragment fragment = new DebugFragment(this);
        loadFragment(fragment, R.id.frameLayout_settingUI_debugReplace);

    }
}
