package com.arsr.arsr.db;

public class Task extends Entity {
    private long id;//任务id
    private int times=0;//当前已经重复次数
    private int dayToRecall=0;//下一次recall的剩余时间
    private int assistTimes=-2;//辅助recall的次数
    private int dayToAssist=-2;//下次辅助recall的剩余时间
    private String name;//任务名 保存的时候默认为分类名+ +任务名
    private long tid;//任务id

    public Task(long id, int times, int dayToRecall, int assistTimes, int dayToAssist, String name, long tid) {
        this.id = id;
        this.times = times;
        this.dayToRecall = dayToRecall;
        this.assistTimes = assistTimes;
        this.dayToAssist = dayToAssist;
        this.name = name;
        this.tid = tid;
    }

    public Task() {

    }

    @Override
    public String toString() {
        return id + " | " + name + " | " + times + "次 | " + dayToRecall + "天 | " + assistTimes + "次 | " + dayToAssist + "天 | " + tid;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getDayToRecall() {
        return dayToRecall;
    }

    public void setDayToRecall(int dayToRecall) {
        this.dayToRecall = dayToRecall;
    }

    public int getAssistTimes() {
        return assistTimes;
    }

    public void setAssistTimes(int assistTimes) {
        this.assistTimes = assistTimes;
    }

    public int getDayToAssist() {
        return dayToAssist;
    }

    public void setDayToAssist(int dayToAssist) {
        this.dayToAssist = dayToAssist;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}