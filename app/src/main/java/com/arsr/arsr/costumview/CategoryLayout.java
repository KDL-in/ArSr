package com.arsr.arsr.costumview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arsr.arsr.R;

/**
 * Created by KundaLin on 17/12/20.
 * 类别的自定义控件
 */

public class CategoryLayout extends CardView {

    private ImageView imageView;

    public CategoryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_category, this);
        init();
//        imageView.setOnClickListener(this);
    }

    private void init() {
        imageView = findViewById(R.id.img_expand);
    }


}
