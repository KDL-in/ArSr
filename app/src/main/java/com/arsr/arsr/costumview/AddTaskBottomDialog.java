package com.arsr.arsr.costumview;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.arsr.arsr.MyApplication;
import com.arsr.arsr.R;
import com.arsr.arsr.activity.MainActivity;
import com.arsr.arsr.db.Tag;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.ListUtil;
import com.arsr.arsr.util.LogUtil;
import com.arsr.arsr.util.ToastUtil;
import com.arsr.arsr.util.UIDataUtil;

import java.util.List;

import me.shaohui.bottomdialog.BaseBottomDialog;

@SuppressLint("ValidFragment")
public class AddTaskBottomDialog extends BaseBottomDialog {
    private final int BIG_N = 100000;
    private ImageView tagImg;
    private EditText taskTv, categoryTv, tagTv, idxTv;
    private Button cancelBtn, ensureBtn;
    //分类-标签数据
    private static List<String> group;
    private static List<List<String>> children;
    private String lastInput = "";
    private String groupTagName = "";
    private View itemView;
    private int groupPosition;
    private AppCompatActivity context;


    @SuppressLint("ValidFragment")
    public AddTaskBottomDialog(AppCompatActivity context, View itemView) {
        this.itemView = itemView;
        this.context = context;
    }


    @Override

    public int getLayoutRes() {
        return R.layout.dialog_addtask;
    }
    private int position;
    @Override
    public void bindView(final View view) {
        //初始化
        tagImg = view.findViewById(R.id.img_addDialog_tag);
        taskTv = view.findViewById(R.id.edit_addTag_name);
        idxTv = view.findViewById(R.id.edit_addDialog_idx);
        UIDataUtil.setConstrain(taskTv, idxTv);
        tagTv = view.findViewById(R.id.edit_addDialog_tag);
        categoryTv = view.findViewById(R.id.edit_addTag_prefix);
        cancelBtn = view.findViewById(R.id.btn_addDialog_cancel);
        ensureBtn = view.findViewById(R.id.btn_addDialog_ensure);
        //获得分类-标签数据
        group = ListUtil.getCategoryNameGroup();
        children = ListUtil.getTagNameChildren(group);
        //监听
        tagImg.setOnClickListener(new OnTagClickListener());//标签按钮
        taskTv.setOnEditorActionListener(new TextView.OnEditorActionListener() {//编辑监听
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                taskTv.clearFocus();
                String curName = taskTv.getText().toString().trim();
                if (!curName.equals(lastInput)) {
                    idxTv.setText("1");
                }
                return false;
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                AddTaskBottomDialog.this.dismiss();
            }
        });
        ensureBtn.setOnClickListener(new View.OnClickListener() {//确认插入
            @Override
            public void onClick(final View v) {

                String category = categoryTv.getText().toString();
                String realTagName = tagTv.getText().toString();
                String realTaskName = taskTv.getText().toString();
                String number = idxTv.getText().toString();
                if (UIDataUtil.checkEmpty(category, realTagName, realTaskName, number)) {
                    ToastUtil.makeToast("输入不能为空");
                    return;
                }
                final String tagName = category + "_" + realTagName;

                final String taskName = category + "_" + realTagName + "-" + realTaskName + "_" + number;
                long id = DBUtil.taskDAO.insert(taskName, tagName);
                if (id == -1) {
                    ToastUtil.makeToast("添加失败：任务已存在");
                    AddTaskBottomDialog.this.dismiss();
                    return;
                }
                if (groupTagName.equals("")) {
                    position = UIDataUtil.insertChildAtFirst(UIDataUtil.KEY_TODAY_TASKS, category, category + " " + realTagName + "-" + realTaskName + " " + number);
                    LogUtil.d(position+"");
                } else {//在分类管理界面被重用，所以要额外处理
                    position = UIDataUtil.insertChildAtFirst(UIDataUtil.KEY_TASKS_CATEGORY, groupTagName, category + " " + realTagName + "-" + realTaskName + " " + number);
                    LogUtil.d(position+"");
                }
                //启动更新数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }finally {
                            ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UIDataUtil.updateUIData(UIDataUtil.TYPE_TASK_CHANGED);
                                }
                            });
                        }

                    }
                }).start();
                AddTaskBottomDialog.this.dismiss();
              /*  ToastUtil.makeSnackbar(itemView, "添加新任务" + taskName + "?", "撤销添加", new ToastUtil.OnSnackbarListener() {
                    @Override
                    public void onUndoClick() {

                        if (groupTagName.equals("")) {
                            UIDataUtil.delete(UIDataUtil.KEY_TODAY_TASKS, position);
                        } else {//在分类管理界面被重用，所以要额外处理
                            UIDataUtil.delete(UIDataUtil.KEY_TASKS_CATEGORY,position);
                        }
                    }

                    @Override
                    public void onDismissed(int event) {
                        if (event != BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_ACTION) {
                            long id = DBUtil.taskDAO.insert(taskName, tagName);
                            if (id == -1) {
                                ToastUtil.makeToast("添加失败：任务已存在");
                                if (groupTagName.equals("")) {
                                    UIDataUtil.delete(UIDataUtil.KEY_TODAY_TASKS, position);
                                } else {
                                    UIDataUtil.delete(UIDataUtil.KEY_TASKS_CATEGORY, position);
                                }
                                return;
                            }
                            UIDataUtil.updateUIData(UIDataUtil.TYPE_TASK_CHANGED);
                        }
                    }

                    @Override
                    public void onShown() {
                        AddTaskBottomDialog.this.dismiss();//关闭
                    }
                });*/
                /*         ToastUtil.makeSnackbar(itemView, "添加新任务" + taskName + "?", "撤销添加", new ToastUtil.OnSnackbarListener() {
                    @Override
                    public void onUndoClick() {

                    }

                    @Override
                    public void onDismissed(int event) {
                        if (event != BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_ACTION) {
                            long id = DBUtil.taskDAO.insert(taskName, tagName);
                            if (id == -1) {
                                ToastUtil.makeToast("添加失败：任务已存在");
                                return;
                            }
                            UIDataUtil.updateUIData(UIDataUtil.TYPE_TASK_CHANGED);
                        }
                        AddTaskBottomDialog.this.dismiss();//关闭
                    }

                    @Override
                    public void onShown() {
                        view.setVisibility(View.INVISIBLE);
                    }
                });*/

            }
        });
        if (!groupTagName.equals("")) {//如果有设置数据
            autoInput(groupTagName.replaceAll("_"," "));
//            groupTagName = "";
        }


    }

    public void setData(String tagName) {
        groupTagName = tagName;
    }


    /**
     * 标签按钮监听，显示标签选择菜单
     */
    private class OnTagClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //显示menu
            PopupMenu popupMenu = new PopupMenu(MyApplication.getContext(), tagImg);
            Menu menu = popupMenu.getMenu();
            for (int i = 1; i <= group.size(); i++) {
                List<String> childList = children.get(i - 1);
                SubMenu subMenu = menu.addSubMenu(i, i, Menu.NONE, group.get(i - 1));
                for (int j = 1; j <= childList.size(); j++) {
                    String name = childList.get(j - 1);
                    subMenu.add(i, i * BIG_N + j, Menu.NONE, name.substring(name.indexOf(' ') + 1));
                }
            }
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new OnPopupMenuItemClickListener());
        }
    }

    /**
     * 分类-标签菜单的监听
     */
    private class OnPopupMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int itemId = item.getItemId();
            if (itemId >= BIG_N) {
                String name = children.get(item.getGroupId() - 1).get(itemId % BIG_N - 1);
                autoInput(name);
            }
            return false;
        }
    }

    private void autoInput(String name) {
        Tag tag = DBUtil.getTag(DBUtil.packing(name));
        taskTv.setText(tag.getPrefix());
        lastInput = tag.getPrefix();
        idxTv.setText(DBUtil.getMaxNOf(tag) + 1 + "");
        categoryTv.setText(DBUtil.getSubstringCategory(name,' '));
        tagTv.setText(DBUtil.getSubstringName(name,' '));
        taskTv.setEnabled(true);
        idxTv.setEnabled(true);
        taskTv.setSelection(taskTv.getText().length());
    }
}