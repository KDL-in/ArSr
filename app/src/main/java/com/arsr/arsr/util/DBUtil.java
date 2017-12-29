package com.arsr.arsr.util;

import org.litepal.LitePal;

/**
 * 数据库操作工具
 * 用于对接数据库，存取数据
 * Created by KundaLin on 17/12/29.
 */

public class DBUtil {
    /**
     * 用于初始化数据库
     */
    public static void initDatabase() {
        LitePal.getDatabase();
    }
}
