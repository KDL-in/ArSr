package com.arsr.arsr.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.BaseTransientBottomBar;
import android.view.View;
import android.widget.EditText;

import com.arsr.arsr.R;
import com.arsr.arsr.activity.MainActivity;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.ToastUtil;
import com.arsr.arsr.util.UIDataUtil;

/**
 * Created by KundaLin on 18/1/7.
 */

public class OnNavAddButtonListener implements View.OnClickListener {
    private MainActivity context;

    public OnNavAddButtonListener(MainActivity context) {
        this.context = context;
    }

    @Override
    public void onClick(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("添加分类");
        final View view = context.getLayoutInflater().inflate(R.layout.item_nav_addcategory, null);
        builder.setView(view);
        final EditText editText = view.findViewById(R.id.edit_navDialog_name);
        UIDataUtil.setConstrain(editText);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = editText.getText().toString();
                if (UIDataUtil.checkEmpty(name)) {
                    ToastUtil.makeToast("输入不能为空");
                    return;
                }
                UIDataUtil.insert(UIDataUtil.KEY_CATEGORY, 0, name);
                ToastUtil.makeSnackbar(v, "确认添加分类" + name, "撤销添加", new ToastUtil.OnSnackbarListener() {
                    @Override
                    public void onUndoClick() {
                        UIDataUtil.delete(UIDataUtil.KEY_CATEGORY, 0);
                    }

                    @Override
                    public void onDismissed(int event) {
                        if (event != BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_ACTION) {
                            long id = DBUtil.categoryDAO.insert(name);
                            if (id == -1) {
                                ToastUtil.makeToast("添加失败,不能重复");
                                return;
                            }
                            UIDataUtil.updateUIData(UIDataUtil.TYPE_CATEGORY_CHANGE);
                        }
                    }

                    @Override
                    public void onShown() {
                        view.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
        builder.show();

    }
}
