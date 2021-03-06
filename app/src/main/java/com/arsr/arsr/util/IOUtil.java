package com.arsr.arsr.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.text.format.DateUtils;

import com.arsr.arsr.MyApplication;
import com.arsr.arsr.R;
import com.arsr.arsr.activity.ActivityCollector;
import com.arsr.arsr.db.Tag;
import com.arsr.arsr.db.Task;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.snatik.storage.Storage;

import java.io.File;
import java.io.IOException;
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
    private static final String EXTERNAL_STORAGE_DIR;
    private static final String BACKUP_DIR;
    private static final String DATA_DIR;
    public static final int FLAG_FEEL_BAD = -1;//微调标志
    public static final int FLAG_FEEL_GOOD = 1;//同上
    private static Storage mStorage;//Io操作对象
    private static SharedPreferences recordPref;//spref操作对象
    private static SharedPreferences.Editor recordEditor;//同上
    private static final int[] UPPER_BOUND = {1, 2, 5, 8, 16, 16};//微调边界
    private static final int[] LOWER_BOUND = {1, 2, 2, 3, 4, 4};//同上
    private static Map<Long, int[]> pit; //pointsInTime缓存数组

    private static SharedPreferences configPref;
    private static SharedPreferences.Editor configEditor;
    public static boolean ifOutputLog = false;

    static {
        mStorage = new Storage(MyApplication.getContext());
        FILES_DIR = mStorage.getInternalFilesDirectory();
        RECALL_DATES_DIR = getDirPath(new String[]{FILES_DIR, "recall_dates"});
        ASSIST_DATES_DIR = getDirPath(new String[]{FILES_DIR, "assist_dates"});
        POINTS_IN_TIME_DIR = getDirPath(new String[]{FILES_DIR, "points_in_time"});
        RECALL_RECORDS_DIR = getDirPath(new String[]{FILES_DIR, "recall_records"});
        EXTERNAL_STORAGE_DIR = mStorage.getExternalStorageDirectory();
        BACKUP_DIR = getDirPath(new String[]{EXTERNAL_STORAGE_DIR, "backup"});
        DATA_DIR = new File(FILES_DIR).getParentFile().getAbsolutePath();
        init();
        recordPref = MyApplication.getContext().getSharedPreferences("adjust_records", Context.MODE_PRIVATE);
        recordEditor = recordPref.edit();
        configPref = MyApplication.getContext().getSharedPreferences("last_exec", Context.MODE_PRIVATE);
        configEditor = configPref.edit();
        pit = new HashMap<>();
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
     *
     * @param task 欲保存的task
     */
    public static void saveRecallDate(Task task) {
        String parentDirs[] = {RECALL_DATES_DIR, DBUtil.getSubstringCategory(task.getName(), '_')};
        String filePath = getFilePath(getDirPath(parentDirs), DBUtil.getSubstringName(task.getName(), '_'));
        String date = DateUtil.dateToString(DateUtil.getToDay());
        setLastExec(date);
        mStorage.appendFile(filePath, " " + date);
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
        String date = DateUtil.dateToString(DateUtil.getToDay());
        setLastExec(date);
        mStorage.appendFile(filePath, " " + date);

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
     *             1 2 3 4 5 6（次的recall需要时间）
     */
    public static void setPointInTimeOf(Tag tag, String args) {
        String category = DBUtil.getSubstringCategory(tag.getName(), '_');
        String name = tag.getName();
        String filePath = getFilePath(getDirPath(new String[]{POINTS_IN_TIME_DIR, category}), name);
        if (args.equals("")) {
            mStorage.createFile(filePath, integersToString(getPointsInTimeOf(null)));
        } else {
            LogUtil.e("pointInTime Of "+tag.getName(),args);
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
     *
     * @param tag      标签
     * @param task     任务
     * @param flagFeel recall效果好坏，对于常量-1,+1
     */
    public static void adjustPointInTimeOf(Tag tag, Task task, int flagFeel) {
        int[] args = getPointsInTimeOf(tag);
        int idx = task.getTimes() - 1;
        if (idx == -1) return;//任务开始的时候第0次
        if (args[idx] + flagFeel <= UPPER_BOUND[idx] && args[idx] + flagFeel >= LOWER_BOUND[idx]) {
            args[task.getTimes() - 1] += flagFeel;
            setPointInTimeOf(tag, integersToString(args));
            //保存微调记录
            saveAdjustRecord(task.getId(), flagFeel);
        }
    }

    /**
     * 通过shardPreferences保存微调记录，以便于回滚
     *
     * @param id       task的id作为key
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
        return recordPref.getInt(id + "", 0);
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
        if (task.getTimes() == 0) return;
        int[] args = getPointsInTimeOf(tag);
        int feelFlag = getAdjustRecord(task.getId());
        args[task.getTimes() - 1] -= feelFlag;
        setPointInTimeOf(tag, integersToString(args));
        //删除记录
        if (feelFlag != 0) removeAdjustRecord(task.getId());
    }

    public static void clearAdjustRecords() {
        recordEditor.clear();
        recordEditor.apply();
    }

    /**
     * 获得recall日期
     */
    public static String[] getRecallDatesOf(String name) {
        String path = getDirPath(new String[]{RECALL_DATES_DIR, DBUtil.getSubstringCategory(name, '_')});
        String filePath = getFilePath(path, DBUtil.getSubstringName(name, '_'));
        String content = mStorage.readTextFile(filePath);
        return content.split(" ");
    }

    /**
     * 获得name的task的assist日期
     */
    public static String[] getAssistDatesOf(String name) {
        String path = getDirPath(new String[]{ASSIST_DATES_DIR, DBUtil.getSubstringCategory(name, '_')});
        String filePath = getFilePath(path, DBUtil.getSubstringName(name, '_'));
        String content = mStorage.readTextFile(filePath);
        return content.split(" ");
    }


    public static CalendarDay getUpdateRecord() {
        String dateStr = recordPref.getString("update_record", "1970-01-01");
        CalendarDay date = CalendarDay.from(DateUtil.stringToDate(dateStr));
        return date;
    }

    public static void setUpdateRecord(String date) {
        recordEditor.putString("update_record", date);
        recordEditor.apply();
    }

    /**
     * 最后一次执行task的时间
     *
     * @return
     */
    public static CalendarDay getLastExec() {
        String dateStr = configPref.getString("last_exec", "1970-01-01");
        CalendarDay date = CalendarDay.from(DateUtil.stringToDate(dateStr));
        return date;
    }

    public static void setLastExec(String date) {
        configEditor.putString("last_exec", date);
        configEditor.apply();
    }
    public static void setTheme(int theme) {
        configEditor.putInt("theme", theme);
        configEditor.apply();
    }

    public static void setBingSwitch(boolean b) {
        configEditor.putBoolean("bing_switch", b);
        configEditor.apply();
    }

    public static boolean getBingSwitch() {
        return configPref.getBoolean("bing_switch", false);
    }
    public static void setBingPicUrl(String url) {
        configEditor.putString("bing_pic_url", url);
        configEditor.apply();
    }
    public static String getBingPicUrl() {
        return configPref.getString("bing_pic_url", null);
    }

    public static int getTheme() {
        return configPref.getInt("theme", R.style.AppTheme);
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
            filePath = getFilePath(getDirPath(new String[]{POINTS_IN_TIME_DIR, category}), name);
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
        mStorage.appendFile(filePath, t.getName());
        //删除十五天前记录
        String dayBefore = DateUtil.getDateStringBeforeToday(16);
        if (mStorage.isFileExist(RECALL_RECORDS_DIR + File.separator + dayBefore)) {
            mStorage.deleteFile(RECALL_RECORDS_DIR + File.separator + dayBefore);
        }

    }

    /**
     * 获取过去某天的recall记录
     *
     * @param date 过去某天的日期，字符串，YYYY-MM-dd
     */
    public static List<Task> getPastRecallRecord(String date) {
        String filePath = getFilePath(RECALL_RECORDS_DIR, date);
        String content = mStorage.readTextFile(filePath);

        String taskNames[] = content.split(System.getProperty("line.separator"));
        List<Task> tasks = new ArrayList<>();
        for (String name :
                taskNames) {
            if (name.equals("")) continue;
            tasks.add(DBUtil.taskDAO.get(name));
        }
        return tasks;
    }

    private static String log_file_path = "";

    public static void outputLog(String tag, String msg) {
        if (log_file_path.equals("")) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = getDirPath(new String[]{Environment.getExternalStorageDirectory().getAbsolutePath(), "arsr_log"});
                log_file_path = getFilePath(path, DateUtil.getDateStringAfterToday(0));
            }
        } else {
            mStorage.appendFile(log_file_path,"========"+tag+"=========");
            mStorage.appendFile(log_file_path,msg);
        }

    }


    public static String readLogFromFile() {
        String s;
        s = mStorage.readTextFile(log_file_path);
        return s;
    }

    public static void clearLog() {
        mStorage.createFile(log_file_path, "");
    }

    private static String backup_dir = "";

    /**
     * 备份数据
     */
    public static void backup() {
        if (mStorage.isDirectoryExists(BACKUP_DIR)) mStorage.deleteDirectory(BACKUP_DIR);
        copyDirectory(DATA_DIR,BACKUP_DIR);
    }

    /**
     * 还原数据
     */
    public static void recovery() {
        if (mStorage.isDirectoryExists(DATA_DIR)) mStorage.deleteDirectory(DATA_DIR);
        copyDirectory(BACKUP_DIR,DATA_DIR);
        ActivityCollector.finishAll();
    }
    /**
     * 复制整个文件夹内容
     */
    public static void copyDirectory(String sourceDir, String targetDir) {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile=file[i];
                // 目标文件
                File targetFile=new
                        File(new File(targetDir).getAbsolutePath()
                        +File.separator+file[i].getName());
                mStorage.copy(sourceFile.getAbsolutePath(),targetFile.getAbsolutePath());
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1=sourceDir + File.separator + file[i].getName();
                // 准备复制的目标文件夹
                String dir2=targetDir + File.separator+ file[i].getName();
                copyDirectory(dir1, dir2);
            }
        }
    }
}

