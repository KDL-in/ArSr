package com.arsr.arsr.util;

import android.widget.EditText;

/**
 * 界面数据操作工具
 * 判断界面数据数据的合法性以及更新数据
 * Created by KundaLin on 18/1/6.
 */

public class UIDataUtil {
    public static boolean isEmpty(EditText edit) {
        return edit.getText().toString().trim().equals("");
    }

    public static void updateUI(String type) {

    }
}
