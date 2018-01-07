package com.arsr.arsr.listener;

import android.content.DialogInterface;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.arsr.arsr.R;
import com.arsr.arsr.activity.MainActivity;
import com.arsr.arsr.activity.TasksInCategoryActivity;
import com.arsr.arsr.adapter.CategoryListRecyclerViewAdapter;
import com.arsr.arsr.db.Category;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.ToastUtil;
import com.arsr.arsr.util.UIDataUtil;

import drawthink.expandablerecyclerview.listener.OnRecyclerViewListener;

/**
 * 分类-标签列表长按
 * Created by KundaLin on 18/1/7.
 */

public class OnCategoryListLongClickListener implements OnRecyclerViewListener.OnItemLongClickListener {
    private MainActivity context;
    private CategoryListRecyclerViewAdapter adapter;
    public OnCategoryListLongClickListener(MainActivity context, CategoryListRecyclerViewAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public void onGroupItemLongClick(int position, int groupPosition, View view) {
        TasksInCategoryActivity.startAction(adapter.getGroupData(groupPosition));
    }

    @Override
    public void onChildItemLongClick(int position, final int groupPosition, final int childPosition, final View view) {
  /*      new AlertDialog.Builder(context)
                .setTitle("删除标签?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();*/
        //长按弹出菜单
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_taglist_menu, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                new AlertDialog.Builder(context)
                        .setTitle("删除标签连同子任务?")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            //确定删除监听
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                view.setVisibility(View.INVISIBLE);
                                ToastUtil.makeSnackbar(view, "确定删除所有子任务？", "恢复标签", new ToastUtil.OnSnackbarListener() {
                                    @Override
                                    public void onUndoClick() {
                                        view.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onDismissed(int event) {
                                        if (event== BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_ACTION)return;
                                        String name = adapter.getChildData(groupPosition, childPosition);
                                        DBUtil.tagDAO.delete(name.replaceAll(" ", "_"));
                                        UIDataUtil.updateUIData(UIDataUtil.TYPE_TAG_CHANGED);
                                    }
                                });
                            }
                        }).show();

                return false;
            }
        });

    }
}
