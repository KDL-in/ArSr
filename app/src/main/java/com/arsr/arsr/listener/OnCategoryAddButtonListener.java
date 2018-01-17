package com.arsr.arsr.listener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.ToastUtil;
import com.arsr.arsr.util.UIDataUtil;

/**
 * 分类-标签列表上的添加按钮
 * Created by KundaLin on 18/1/6.
 */

public class OnCategoryAddButtonListener implements View.OnClickListener {
    private View parent;
    private Context context;

    public OnCategoryAddButtonListener(View parent, Context mContext) {
        this.parent = parent;
        context = mContext;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {
        final TextView categoryTv = parent.findViewById(R.id.tv_categoryList_groupTitle);
        //自定义布局
        LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_addtag, null);
        //相关view
        final EditText tagNameEdit = view.findViewById(R.id.edit_addTag_name);
        final EditText tagPrefixEdit = view.findViewById(R.id.edit_addTag_prefix);
        final EditText tagNote = view.findViewById(R.id.edit_addTag_note);
        UIDataUtil.setConstrain(tagNameEdit, tagPrefixEdit);
        //弹出窗口
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {//确认添加
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = tagNameEdit.getText().toString().trim();
                final String prefix = tagPrefixEdit.getText().toString().trim();
                final String note = tagNote.getText().toString().trim();
                if (UIDataUtil.checkEmpty(name, prefix)) {
                    ToastUtil.makeToast("名字、前缀不能为空");
                    return;
                }
                final String category = categoryTv.getText().toString();
                //显示
                long id = DBUtil.tagDAO.insert(name, prefix, category, note);
                if (id == -1) {
                    ToastUtil.makeToast("添加失败，标签已存在");
                    return;
                }
                int position = UIDataUtil.insertChildAtFirst(UIDataUtil.KEY_CATEGORY_TAG_LIST, category, category + " " + name);

                /*//确认
                ToastUtil.makeSnackbar(parent, "确定插入标签" + name, "撤销插入", new ToastUtil.OnSnackbarListener() {
                    @Override
                    public void onUndoClick() {
                        UIDataUtil.delete(UIDataUtil.KEY_CATEGORY_TAG_LIST, position);
                    }

                    @Override
                    public void onDismissed(int event) {
                        if (event == BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_ACTION) {
                            return;
                        }
                        long id = DBUtil.tagDAO.insert(name, prefix, category, note);
                        if (id == -1) {
                            ToastUtil.makeToast("添加失败，标签已存在");
                            UIDataUtil.delete(UIDataUtil.KEY_CATEGORY_TAG_LIST, position);
                            return;
                        }
                        UIDataUtil.updateList(UIDataUtil.KEY_CATEGORY_TAG_LIST);//更新数据库
                    }

                    @Override
                    public void onShown() {

                    }
                });*/
/*                ToastUtil.makeSnackbar(parent, "确定插入标签" + name, "撤销插入", new ToastUtil.OnSnackbarListener() {
                    @Override
                    public void onUndoClick() {

                    }

                    @Override
                    public void onDismissed(int event) {
                        if (event == BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_ACTION) {
                            return;
                        }
                        long id = DBUtil.tagDAO.insert(name, prefix, category, note);
                        if (id == -1) {
                            ToastUtil.makeToast("添加失败，标签已存在");
                            return;
                        }
                        UIDataUtil.updateList(UIDataUtil.KEY_CATEGORY_TAG_LIST);//插入
                    }

                    @Override
                    public void onShown() {

                    }
                });*/


            }
        });
        builder.show();
    }
}
