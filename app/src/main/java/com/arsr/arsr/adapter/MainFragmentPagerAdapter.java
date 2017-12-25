package com.arsr.arsr.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.arsr.arsr.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager和Fragment之间的适配器
 * Created by KundaLin on 17/12/23.
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> views;
    private final static int COUNT = 2;
    private final int tags[] = {R.drawable.ic_today_task, R.drawable.ic_create_at};
    private Context mContext;

    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.views = new ArrayList<>();
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return views.get(position);
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    //    @Override
//    public CharSequence getPageTitle(int position) {
//        Drawable image = mContext.getResources().getDrawable(tags[position]);
//        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//        SpannableString sb = new SpannableString(" ");
//        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return sb;
//    }

    /**
     * 自定义tab布局
     * @param position
     * @return
     */
    public View getTabView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tab, null);
        ImageView img = view.findViewById(R.id.img_tab);
        img.setImageResource(tags[position]);
        return view;
    }

    public void addFragment(Fragment taskListFragment) {
        views.add(taskListFragment);
    }
}
