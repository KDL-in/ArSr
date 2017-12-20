package com.arsr.arsr.costumview;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.arsr.arsr.R;

/**
 * Created by KundaLin on 17/12/20.
 */

public class EntryLayout extends CardView {
    public EntryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_entry, this);
    }

}
