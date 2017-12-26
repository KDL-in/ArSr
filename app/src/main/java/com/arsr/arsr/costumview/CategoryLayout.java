package com.arsr.arsr.costumview;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.arsr.arsr.R;

/**
 * Created by KundaLin on 17/12/20.
 * 类别的自定义控件
 */

public class CategoryLayout extends CardView {

    private ImageView imageView;
    private TextView categoryTextView;//类别的名字view
    public CategoryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_tasklist_group, this);
        init();
//        imageView.setOnClickListener(this);
    }

    private void init() {
        imageView = findViewById(R.id.img_taskList_groupImgButton);
        categoryTextView = findViewById(R.id.tv_taskList_groupTitle);
    }

}
