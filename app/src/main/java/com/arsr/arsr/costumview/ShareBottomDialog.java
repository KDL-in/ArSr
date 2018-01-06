package com.arsr.arsr.costumview;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.arsr.arsr.MyApplication;
import com.arsr.arsr.R;
import com.arsr.arsr.activity.MainActivity;
import com.arsr.arsr.db.Tag;
import com.arsr.arsr.db.Task;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.ListUtil;
import com.arsr.arsr.util.ToastUtil;
import com.arsr.arsr.util.UIDataUtil;

import java.util.List;

import me.shaohui.bottomdialog.BaseBottomDialog;

public class ShareBottomDialog extends BaseBottomDialog {
    private final int BIG_N = 100000;
    private ImageView tagImg;
    private EditText taskTv, categoryTv, tagTv, idxTv;
    private Button cancelBtn,ensureBtn;
    //分类-标签数据
    private static List<String> group;
    private static List<List<String>> children;
    private String lastInput = "";

    public ShareBottomDialog() {
    }


    @Override

    public int getLayoutRes() {
        return R.layout.dialog_layout;
    }

    @Override
    public void bindView(View v) {
        // do any thing you want
        //初始化
        tagImg = v.findViewById(R.id.img_addDialog_tag);
        taskTv = v.findViewById(R.id.edit_addDialog_name);
        idxTv = v.findViewById(R.id.edit_addDialog_idx);
        tagTv = v.findViewById(R.id.edit_addDialog_tag);
        categoryTv = v.findViewById(R.id.edit_addDialog_category);
        cancelBtn = v.findViewById(R.id.btn_addDialog_cancel);
        ensureBtn = v.findViewById(R.id.btn_addDialog_ensure);
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
                ShareBottomDialog.this.dismiss();
            }
        });
        ensureBtn.setOnClickListener(new View.OnClickListener() {//确认插入
            @Override
            public void onClick(View v) {
                DBUtil.taskDAO.display();
                String tagName = categoryTv.getText() + "_" + tagTv.getText();
                String taskName = categoryTv.getText() + "_" + taskTv.getText() + "_" + idxTv.getText();
                long id =DBUtil.taskDAO.insert(taskName, tagName);
                UIDataUtil.updateUI("today_tasks");
                ShareBottomDialog.this.dismiss();//关闭
            }
        });


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
                    subMenu.add(i, i * BIG_N + j, Menu.NONE, name.substring(name.indexOf(' ')+1));
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
                Tag tag = DBUtil.getTag(DBUtil.packing(name));
                taskTv.setText(tag.getPrefix());
                lastInput = tag.getPrefix();
                idxTv.setText(DBUtil.getMaxNOf(tag) + 1 + "");
                categoryTv.setText(group.get(item.getGroupId() - 1));
                tagTv.setText(item.getTitle());
                taskTv.setEnabled(true);
                idxTv.setEnabled(true);
                taskTv.setSelection(taskTv.getText().length());
            }
            return false;
        }
    }
}