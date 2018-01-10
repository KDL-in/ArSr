package com.arsr.arsr.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.arsr.arsr.MyApplication;
import com.arsr.arsr.db.Tag;
import com.arsr.arsr.db.Task;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.snatik.storage.Storage;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件存取工具
 * Created by KundaLin on 17/12/29.
 */

public class IOUtil {

    private static final String DEFAULT_POINT_IN_TIMES = "1 2 4 6 7 8";//默认recall时间点
    private static final int[] ASSIST_POINT_IN_TIME = {1, 3, 5, 8};//辅助recall时间点
    private static final String ASSIST_DATES_DIR;//文件夹路径
    private static final String FILES_DIR;//同上
    private static final String RECALL_DATES_DIR;//同上
    private static final String POINTS_IN_TIME_DIR;//同上

    private static final String RECALL_RECORDS_DIR;
    public static final int FLAG_FEEL_BAD =-1;//微调标志
    public static final int FLAG_FEEL_GOOD =1;//同上
    private static Storage mStorage;//Io操作对象
    private static SharedPreferences recordPref;//spref操作对象
    private static SharedPreferences.Editor recordEditor;//同上
    private static final int[] UPPER_BOUND = {1, 2, 5, 8, 16, 16};//微调边界
    private static final int[] LOWER_BOUND = {1, 2, 2, 3, 4, 4};//同上
    private static Map<Long,int[]> pit; //pointsInTime缓存数组
    static {
        mStorage = new Storage(MyApplication.getContext());
        FILES_DIR = mStorage.getInternalFilesDirectory();
        RECALL_DATES_DIR = getDirPath(new String[]{FILES_DIR, "recall_dates"});
        ASSIST_DATES_DIR = getDirPath(new String[]{FILES_DIR, "assist_dates"});
        POINTS_IN_TIME_DIR = getDirPath(new String[]{FILES_DIR, "points_in_time"});
        RECALL_RECORDS_DIR = getDirPath(new String[]{FILES_DIR, "recall_records"});
        init();
        recordPref = MyApplication.getContext().getSharedPreferences("adjust_records", Context.MODE_PRIVATE);
        recordEditor = recordPref.edit();
        pit=new HashMap<>();
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
//        mStorage.createFile(filePath, " 2017-12-20 2017-12-22 2017-12-26 2018-01-01 2018-01-08" );
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
    public static void cancelSaveAssistDate(Task task) {
        String parentDirs[] = {ASSIST_DATES_DIR, DBUtil.getSubstringCategory(task.getName(), '_')};
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
     * 设置tag的recall时间，默认设置为等于总体的recall时间
     *
     * @param tag  标签
     * @param args 时间点参数，为""时代表默认设置,格式是
     *                 1 2 3 4 5 6（次的recall需要时间）
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
     *
     * @param content recall时间点，格式是
     *                1 2 3 4 5 6（次的recall需要时间）
     * @return 长度为6的一维int数组
     */
    private static int[] stringToIntegers(String content) {
        int[] arr = new int[6];
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
     * @param task 任务
     * @param flagFeel recall效果好坏，对于常量-1,+1
     */
    public static void adjustPointInTimeOf(Tag tag, Task task, int flagFeel) {
        int []args = getPointsInTimeOf(tag);
        int idx = task.getTimes() - 1;
        if (idx==-1)return;//任务开始的时候第0次
        if (args[idx] + flagFeel <= UPPER_BOUND[idx] && args[idx] + flagFeel >= LOWER_BOUND[idx]) {
            args[task.getTimes()-1] += flagFeel;
            setPointInTimeOf(tag,integersToString(args));
            //保存微调记录
            saveAdjustRecord(task.getId(), flagFeel);
        }
    }

    /**
     * 通过shardPreferences保存微调记录，以便于回滚
     *
     * @param id task的id作为key
     * @param flagFeel 上调或下调
     */
    private static void saveAdjustRecord(long id, int flagFeel) {
        recordEditor.putInt(id + "", flagFeel);
        recordEditor.apply();
    }

    /**
     * 获得某id的操作记录
     */
    private static int getAdjustRecord(long id) {
        return recordPref.getInt(id+"",0);
    }
    /**
     * 删除记录
     */
    private static void removeAdjustRecord(long id) {
        recordEditor.remove(id + "");
        recordEditor.apply();
    }
    /**
     * 取消最后一次微调
     */
    public static void cancelAdjustPointInTimeOf(Tag tag, Task task) {
        if (task.getTimes()==0)return;
        int []args = getPointsInTimeOf(tag);
        int feelFlag = getAdjustRecord(task.getId());
        args[task.getTimes()-1] -= feelFlag;
        setPointInTimeOf(tag,integersToString(args));
        //删除记录
        if (feelFlag!=0)removeAdjustRecord(task.getId());
    }

    public static void clearAdjustRecords() {
        recordEditor.clear();
        recordEditor.apply();
    }
    public static String[] getRecallDatesOf(String name) {
        String path = getDirPath(new String[]{RECALL_DATES_DIR, DBUtil.getSubstringCategory(name, '_')});
        String filePath = getFilePath(path, DBUtil.getSubstringName(name, '_'));
        String content = mStorage.readTextFile(filePath);
        return content.split(" ");
    }





    public static CalendarDay getUpdateRecord() {
        String dateStr = recordPref.getString("update_record", "1970-01-01");
        CalendarDay date = CalendarDay.from(DateUtil.stringToDate(dateStr));
        return date;
    }
    public static void setUpdateRecord(String today) {
        recordEditor.putString("update_record", today);
        recordEditor.apply();
    }


    /**
     * 获取id为tid的标签的时间点数组
     */
    public static int[] getPointsInTimeOf(long tid) {
        int rPit[];
        if (pit.containsKey(tid)) {
            rPit = pit.get(tid);
        } else {
            rPit = IOUtil.getPointsInTimeOf(DBUtil.tagDAO.get(tid));
            pit.put(tid, rPit);
        }
        return rPit;
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
     * 获取assist时间点
     */
    public static int[] getPointsInTimeOfAssist() {
        return ASSIST_POINT_IN_TIME;
    }

    /**
     * 保存date天recall的完成情况
     */
    public static void saveRecallRecord(String date, Task t) {
        String filePath = getFilePath(RECALL_RECORDS_DIR, date);
        mStorage.appendFile(filePath,t.getName());
        //删除十五天前记录
        String dayBefore = DateUtil.getDateStringBeforeToday(16);
        if (mStorage.isFileExist(RECALL_RECORDS_DIR + File.separator + dayBefore)){
            mStorage.deleteFile(RECALL_RECORDS_DIR + File.separator + dayBefore);
        }

    }
    /**
     *  获取过去某天的recall记录
     * @param date 过去某天的日期，字符串，YYYY-MM-dd
     */
    public static void getPastRecallRecord(String date) {
        String filePath = getFilePath(RECALL_RECORDS_DIR, date);
        String content = mStorage.readTextFile(filePath);
        //todo 解析
    }


}
