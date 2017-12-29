package com.arsr.arsr.util;

import com.arsr.arsr.MyApplication;
import com.snatik.storage.Storage;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文件存取工具
 * Created by KundaLin on 17/12/29.
 */

public class IOUtil {
    private static final int DIR_POINT_IN_TIME = 0;
    private static final int DEFAULT_FILE_PONIT_IN_TIME = 10;
    private static final int FILE_IN_POINT_TIME = 11;
    private static final int FILE_IN_RECALL_DATES = 12;
    private static final String DEFAULT_REPETITION_TIMES = "1 2 4 6 7 8";
    private static final int DIR_RECALL_TIMES = 1;
    private static Storage mStorage;
    private static final String FILES_PATH;

    static {
        mStorage = new Storage(MyApplication.getContext());
        FILES_PATH = mStorage.getInternalFilesDirectory();
    }

    /**
     * 保存完成任务的时间
     * @param category
     * @param tag
     * @param name
     */
    public static void saveRecallDate(String category, String tag, String name) {
        String filePath = getFilePath(FILE_IN_RECALL_DATES, category, tag, name);
        mStorage.appendFile(filePath,DateUtil.dateToString(DateUtil.getToDay())+" ");
    }

    public static List<Date> getRecallDate(String category, String tag, String name) throws ParseException {
        String filePath = getFilePath(FILE_IN_RECALL_DATES, category, tag, name);
        String content = mStorage.readTextFile(filePath);
        List<Date> list= stringToDates(content);
        return list;
    }

    /**
     * 从string中提取出日期数组
     * @param content 包含几个date格式字符串的字符串
     * @return date列表
     * @throws ParseException
     */
    private static List<Date> stringToDates(String content) throws ParseException {
        List<Date> dates = new ArrayList<>();
        String arr[] = content.split(" ");
        for (String date :
                arr) {
            dates.add(DateUtil.stringToDate(date));
        }
        return dates;
    }

    /**
     * 设置某标签recall周期
     *
     * @param category    分类
     * @param tag         标签
     * @param pointInTime 周期数组字符串
     */
    public static void setRepetitionTimes(String category, String tag, String pointInTime) {
        mStorage.createFile(getFilePath(DIR_POINT_IN_TIME, category, tag), pointInTime);
    }

    public static void setRepetitionTimes(String pointInTime) {
        mStorage.createFile(getFilePath(DEFAULT_FILE_PONIT_IN_TIME, "", ""), pointInTime);
    }

    public static int[] getRepetitionTimes(String category, String tag) {
        String content = mStorage.readTextFile(getFilePath(FILE_IN_POINT_TIME, category, tag));
        int[] arr = stringToIntegers(content);
        return arr;
    }

    public static int[] getRepetitionTimes() {
        String content = mStorage.readTextFile(getFilePath(DEFAULT_FILE_PONIT_IN_TIME, "", ""));
        int[] arr = stringToIntegers(content);
        return arr;
    }

    /**
     * 调整recall时间
     *
     * @param category
     * @param tag
     * @param index    第几次的时间点，下标从0开始
     * @param days     调整为days天
     */
    public static void adjustRepetitionTime(String category, String tag, int index, int days) {
        int repetitionTime[] = getRepetitionTimes(category, tag);
        repetitionTime[index] = days;
        setRepetitionTimes(category, tag, arrToString(repetitionTime));
    }

    public static void adjustRepetitionTime(int index, int days) {
        int repetitionTime[] = getRepetitionTimes();
        repetitionTime[index] = days;
        setRepetitionTimes(arrToString(repetitionTime));
    }

    /**
     * 还原recall时间点
     */
    public static void resetRepetitionTimes(String category, String tag) {
        setRepetitionTimes(category, tag, DEFAULT_REPETITION_TIMES);

    }
    public static void resetRepetitionTimes() {
        setRepetitionTimes(DEFAULT_REPETITION_TIMES);

    }


    /**
     * 将string中的数字提取出来
     * //todo 这种转换必须保证数据录入绝对正确，意味着界面设置数据要求严格合法检测
     *
     * @param content
     * @return
     */
    private static int[] stringToIntegers(String content) {
        int[] arr = new int[6];
        int j = 0;
        for (int i = 0; i < content.length(); i += 2) {
            arr[j++] = content.charAt(i) - '0';
        }
        return arr;
    }

    private static String arrToString(int[] repetitionTime) {
        String str = "";
        for (int a :
                repetitionTime) {
            str += a + " ";
        }
        return str.substring(0, str.length() - 1);
    }

    /**
     * 获取各种文件路径
     *
     * @param type
     * @param category
     * @param tag
     * @return 文件路径
     */
    public static String getFilePath(int type, String category, String tag) {
        String path = "";
        String name = "";
        switch (type) {
            case DIR_POINT_IN_TIME:
                path = getDirPath(DIR_POINT_IN_TIME);
                name = category + "_" + tag;
                break;

            case DEFAULT_FILE_PONIT_IN_TIME:
                path = getDirPath(DIR_POINT_IN_TIME);
                name = "total";
                break;
        }
        checkFilePath(path,name);
        return path + File.separator + name;
    }

    private static String getFilePath(int type, String category, String tag, String name) {
        String path = "";
        switch (type) {
            case FILE_IN_RECALL_DATES:
                path = getDirPath(DIR_RECALL_TIMES);
                break;
        }
        checkFilePath(path,name);
        return path + File.separator + name;
    }


    /**
     * 获取各种文件夹路径
     *
     * @return 该类型文件夹路径
     */
    private static String getDirPath(int type) {
        String rPath = "";
        switch (type) {
            case DIR_POINT_IN_TIME:
                rPath = FILES_PATH + File.separator + "point_in_time";
                break;
            case DIR_RECALL_TIMES:
                rPath = FILES_PATH + File.separator + "recall_dates";
                break;
        }
        checkDirPath(rPath);
        return rPath;
    }

    /**
     * 检测路径是否存在，不存在则创建
     *
     * @param rPath 输入路径
     */
    private static void checkDirPath(String rPath) {
        if (mStorage.isDirectoryExists(rPath)) {
            return;
        }
        mStorage.createDirectory(rPath);
    }
    private static void checkFilePath(String path,String name) {
        if (mStorage.isFileExist(path+File.separator+name)) {
            return;
        }
        mStorage.createFile(path,name);
    }
}
