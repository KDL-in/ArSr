package com.arsr.arsr.listener;

import android.content.DialogInterface;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.arsr.arsr.R;
import com.arsr.arsr.activity.TasksInCategoryActivity;
import com.arsr.arsr.adapter.TasksInCategoryRecyclerViewAdapter;
import com.arsr.arsr.costumview.AddTaskBottomDialog;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.ToastUtil;
import com.arsr.arsr.util.UIDataUtil;
import com.arsr.mexpandablerecyclerview.listener.OnRecyclerViewListener;


/**
 * Created by KundaLin on 18/1/9.
 */

public class OnTicListLongClickListener implements OnRecyclerViewListener.OnItemLongClickListener {
    private TasksInCategoryActivity context;
    private TasksInCategoryRecyclerViewAdapter adapter;

    public OnTicListLongClickListener(TasksInCategoryActivity context, TasksInCategoryRecyclerViewAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public void onGroupItemLongClick(final int position, final int groupPosition, final View view) {
        //长按弹出菜单
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_ticlist_itemmenu, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_ticlist_deleteTag:
                        new AlertDialog.Builder(context)
                                .setTitle("删除标签连同子任务?")
                                .setNegativeButton("取消", null)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    //确定删除监听
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        view.performClick();
                                        view.setVisibility(View.INVISIBLE);
                                        ToastUtil.makeSnackbar(view, "确定删除所有子任务？", "恢复标签", new ToastUtil.OnSnackbarListener() {
                                            @Override
                                            public void onUndoClick() {
                                                view.setVisibility(View.VISIBLE);
                                                view.performClick();
                                            }

                                            @Override
                                            public void onDismissed(int event) {
                                                if (event== BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_ACTION)return;
                                                view.setVisibility(View.VISIBLE);
                                                String name = adapter.getGroupData(groupPosition);
                                                DBUtil.tagDAO.delete(name.replaceAll(" ", "_"));
                                                UIDataUtil.updateUIData(UIDataUtil.TYPE_TAG_CHANGED);
                                            }

                                            @Override
                                            public void onShown() {

                                            }
                                        });
                                    }
                                }).show();
                        break;
                    case R.id.item_ticlist_addTask:
                        AddTaskBottomDialog dialog = new AddTaskBottomDialog(context, view);
                        dialog.setData(adapter.getGroupData(groupPosition));
                        dialog.show(context.getSupportFragmentManager());
                        break;
                }


                return false;
            }
        });

    }

    @Override
    public void onChildItemLongClick(final int position, final int groupPosition, final int childPosition, final View view) {
        //长按弹出菜单
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_taglist_menu, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                new AlertDialog.Builder(context)
                        .setTitle("删除任务?")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            //确定删除监听
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String name = UIDataUtil.delete(UIDataUtil.KEY_TASKS_CATEGORY, position);
                                ToastUtil.makeSnackbar(view, "确定删除任务？", "恢复任务", new ToastUtil.OnSnackbarListener() {
                                    @Override
                                    public void onUndoClick() {
                                        UIDataUtil.insert(UIDataUtil.KEY_TASKS_CATEGORY,position,name);
                                    }

                                    @Override
                                    public void onDismissed(int event) {
                                        if (event== BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_ACTION)return;
                                        DBUtil.taskDAO.delete(name);
                                        UIDataUtil.updateUIData(UIDataUtil.TYPE_TASK_CHANGED);
                                    }

                                    @Override
                                    public void onShown() {

                                    }
                                });
                            }
                        }).show();

                return false;
            }
        });
    }
}
