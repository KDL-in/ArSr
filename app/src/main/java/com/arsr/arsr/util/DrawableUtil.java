package com.arsr.arsr.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.Toast;

import com.arsr.arsr.MyApplication;
import com.arsr.arsr.R;
import com.arsr.arsr.activity.TaskActivity;
import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by KundaLin on 18/1/8.
 */

public class DrawableUtil {
    public static int themeId = IOUtil.getTheme();
    public static boolean themeChanged = false;
    public static boolean isOpenBingPic;
    static {
        isOpenBingPic = IOUtil.getBingSwitch();
    }
    /**
     * drawable上色方案
     */
    public static Drawable tintDrawable(AppCompatActivity context, Drawable drawable, int colorId) {
        int color = context.getResources().getColor(colorId);
        ColorStateList colors = ColorStateList.valueOf(color);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    /**
     * 从图片获取drawable对象
     */
    public static Drawable getDrawable(AppCompatActivity context, int id) {
        return context.getResources().getDrawable(id);
    }

    /**
     * 从图片id获取颜色
     */
    public static int getColor(AppCompatActivity context, int id) {
        return context.getResources().getColor(id);
    }

    /**
     * 从attr中获取颜色
     */
    public static int getColorFromAttr(Context context, int id) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(id, typedValue, true);
        return typedValue.data;
    }


    public static void setTheme(int theme) {
        IOUtil.setTheme(theme);
        themeId = theme;
        themeChanged = true;
    }

    public static void openBingPic(final AppCompatActivity context) {
        isOpenBingPic = true;
        IOUtil.setBingSwitch(true);
        HttpUtil.sendOkHttpRequest("http://guolin.tech/api/bing_pic", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                ToastUtil.makeToast("获取bing图片失败，请检测网络");
//                LogUtil.d("获取bing图片失败，请检测网络");
                closeBingPic();
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.makeToast("获取bing图片失败，请检测网络");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                IOUtil.setBingPicUrl(response.body().string());
            }
        });
    }

    public static void closeBingPic() {
        isOpenBingPic = false;
        IOUtil.setBingSwitch(false);
    }

    public static void setImg(Context context, ImageView bgImg) {
        String url = IOUtil.getBingPicUrl();

        if (DrawableUtil.isOpenBingPic && url != null) {
            Glide.with(context).load(url).into(bgImg);
        } else {
            Glide.with(context).load(R.drawable.task_background).into(bgImg);
        }
    }
}
