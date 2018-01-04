package com.arsr.arsr.util;

import com.arsr.arsr.MyApplication;
import com.arsr.arsr.db.Tag;
import com.arsr.arsr.db.Task;
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

    private static final String DEFAULT_POINT_IN_TIMES = "0 1 2 4 6 7 8";
    private static final String ASSIST_DATES_DIR;
    private static final String POINTS_IN_TIME_DIR;
    public static final int FLAG_FEEL_BAD =-1;
    public static final int FLAG_FEEL_GOOD =1;

    private static Storage mStorage;
    private static final String FILES_DIR;
    private static final String RECALL_DATES_DIR;

    static {
        mStorage = new Storage(MyApplication.getContext());
        FILES_DIR = mStorage.getInternalFilesDirectory();
        RECALL_DATES_DIR = getDirPath(new String[]{FILES_DIR, "recall_dates"});
        ASSIST_DATES_DIR = getDirPath(new String[]{FILES_DIR, "assist_dates"});
        POINTS_IN_TIME_DIR = getDirPath(new String[]{FILES_DIR, "points_in_time"});
        init();
    }

    private static void init() {
        //总体recall时间点配置文件
        String filePath = POINTS_IN_TIME_DIR + File.separator + "total";
        if (!mStorage.isFileExist(filePath)) {
            mStorage.createFile(filePath, DEFAULT_POINT_IN_TIMES);
        }
    }

/*    public static List<Date> getRecallDate(String category, String tag, String name) throws ParseException {
        String filePath = getFilePath(FILE_IN_RECALL_DATES, category, tag, name);
        String content = mStorage.readTextFile(filePath);
        List<Date> list = stringToDates(content);
        return list;
    }*/

    /**
     * 从string中提取出日期数组
     *
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

/*    *//**
     * 设置某标签recall周期
     *
     * @param category    分类
     * @param tag         标签
     * @param pointInTime 周期数组字符串
     *//*
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
    }*/
/*
    *//**
     * 调整recall时间
     *
     * @param category
     * @param tag
     * @param index    第几次的时间点，下标从0开始
     * @param days     调整为days天
     *//*
    public static void adjustRepetitionTime(String category, String tag, int index, int days) {
        int repetitionTime[] = getRepetitionTimes(category, tag);
        repetitionTime[index] = days;
        setRepetitionTimes(category, tag, integersToString(repetitionTime));
    }

    public static void adjustRepetitionTime(int index, int days) {
        int repetitionTime[] = getRepetitionTimes();
        repetitionTime[index] = days;
        setRepetitionTimes(integersToString(repetitionTime));
    }

    */

    /**
     * 还原recall时间点
     *//*
    public static void resetRepetitionTimes(String category, String tag) {
        setRepetitionTimes(category, tag, DEFAULT_REPETITION_TIMES);

    }

    public static void resetRepetitionTimes() {
        setRepetitionTimes(DEFAULT_REPETITION_TIMES);

    }*/



    //=========================================================================

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

    /**
     * 生成文件夹，返回路径
     */
    private static String getDirPath(String[] dirs) {
        String path = "";
        for (String s :
                dirs) {
            path += s + File.separator;
        }
        checkDirPath(path);
        return path.substring(0, path.length() - 1);
    }

    /**
     * 保存更新task的时间点
     * @param task 欲保存的task
     */
    public static void saveRecallDate(Task task) {
        String parentDirs[] = {RECALL_DATES_DIR, DBUtil.getSubstringCategory(task.getName(), '_')};
        String filePath = getFilePath(getDirPath(parentDirs), DBUtil.getSubstringName(task.getName(), '_'));
        mStorage.appendFile(filePath, " "+ DateUtil.dateToString(DateUtil.getToDay()) );
    }

    /**
     * 取消最后一次时间点
     */
    public static void cancelSaveRecallDate(Task task) {
        String parentDirs[] = {RECALL_DATES_DIR, DBUtil.getSubstringCategory(task.getName(), '_')};
        String filePath = getFilePath(getDirPath(parentDirs), DBUtil.getSubstringName(task.getName(), '_'));
        String content = mStorage.readTextFile(filePath);
        int lastIndex = content.lastIndexOf(' ');
        mStorage.createFile(filePath, content.substring(0, lastIndex));
    }
    public static void saveAssistDate(Task task) {
        String parentDirs[] = {ASSIST_DATES_DIR, DBUtil.getSubstringCategory(task.getName(), '_')};
        String filePath = getFilePath(getDirPath(parentDirs), DBUtil.getSubstringName(task.getName(), '_'));
        mStorage.appendFile(filePath, " "+DateUtil.dateToString(DateUtil.getToDay()));

    }

    /**
     * 查找文件，并返回，文件不存在则创建
     *
     * @param path          所在目录
     * @param substringName 文件名
     * @return 文件路径
     */
    private static String getFilePath(String path, String substringName) {
        String filePath = path + File.separator + substringName;
        checkFilePath(filePath);
        return filePath;
    }

    /**
     * 检查文件是否需要创建
     */
    private static void checkFilePath(String filePath) {
        if (mStorage.isFileExist(filePath)) {
            return;
        }
        mStorage.createFile(filePath, "");
    }


    /**
     * 获取标签tag的recall时间点,tag为null时默认获取总的recall时间点
     *
     * @param tag 标签
     * @return 时间点字符串
     */
    public static int[] getPointsInTimeOf(Tag tag) {
        String content = "";
        String filePath = "";
        if (tag == null) {
            filePath = getFilePath(POINTS_IN_TIME_DIR, "total");
        } else {
            String category = DBUtil.getSubstringCategory(tag.getName(), '_');
            String name = tag.getName();
            filePath=getFilePath(getDirPath(new String[]{POINTS_IN_TIME_DIR, category}), name);
        }
        content = mStorage.readTextFile(filePath);
        return stringToIntegers(content);
    }

    /**
     * 设置tag的recall时间，默认设置为等于总体的recall时间
     *
     * @param tag  标签
     * @param args 时间点参数，为""时代表默认设置,格式是
     *                修改标志 1 2 3 4 5 6（次的recall需要时间）
     */
    public static void setPointInTimeOf(Tag tag, String args) {
        String category = DBUtil.getSubstringCategory(tag.getName(), '_');
        String name = tag.getName();
        String filePath = getFilePath(getDirPath(new String[]{POINTS_IN_TIME_DIR, category}), name);
        if (args.equals("")) {
            mStorage.createFile(filePath, integersToString(getPointsInTimeOf(null)));
        } else {
            mStorage.createFile(filePath, args);
        }
    }

    /**
     * 将string中的数字提取出来
     * //todo 这种转换必须保证数据录入绝对正确，意味着界面设置数据要求严格合法检测
     *
     * @param content recall时间点，格式是
     *                修改标志 1 2 3 4 5 6（次的recall需要时间）
     * @return 长度为7的一维int数组
     */
    private static int[] stringToIntegers(String content) {
        int[] arr = new int[7];
        String str[] = content.split(" ");
        for (int i = 0; i < str.length; i++) {
            arr[i] = Integer.parseInt(str[i]);
        }
        return arr;
    }

    private static String integersToString(int[] arr) {
        String str = "";
        for (int a :
                arr) {
            str += String.valueOf(a) + " ";
        }
        return str.substring(0, str.length() - 1);
    }
    public static void test() {
//        mStorage.createFile(FILES_DIR + File.separator + "test", "1");
//        mStorage.createFile(FILES_DIR + File.separator + "test", "2");
    }

    /**
     * 微调tag某次recall时间点
     * @param tag 标签
     * @param times 第几次
     * @param flagFeel recall效果好坏，对于常量-1,+1
     */
    public static void adjustPointInTimeOf(Tag tag, int times, int flagFeel) {
        int []args = getPointsInTimeOf(tag);
        args[0] = flagFeel;
        args[times] += flagFeel;
        setPointInTimeOf(tag,integersToString(args));
    }

    /**
     * 取消最后一次微调
     */
    public static void cancelAdjustPointInTimeOf(Tag tag, int times) {
        int []args = getPointsInTimeOf(tag);
        args[times] -= args[0];
        args[0] = 0;
        setPointInTimeOf(tag,integersToString(args));
    }
}
