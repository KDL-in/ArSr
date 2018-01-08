package com.arsr.arsr.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;

import com.arsr.arsr.R;

/**
 * Created by KundaLin on 18/1/8.
 */

public class DrawableUtil {
    /**
     * drawable上色方案
     */
    public static Drawable tintDrawable(AppCompatActivity context,Drawable drawable, int colorId) {
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
    public static int getColor(AppCompatActivity context,int id) {
        return context.getResources().getColor(id);
    }
}
