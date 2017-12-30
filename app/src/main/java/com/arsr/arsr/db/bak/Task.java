/*
package com.arsr.arsr.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

*/
/**
 * 数据库实体类 任务
 * <p>
 * Created by KundaLin on 17/12/29.
 *//*


public class Task extends DataSupport {
    private int id;//任务id
    @Column(nullable = false, defaultValue = "0")
    private int times;//当前已经重复次数
    @Column(nullable = false, defaultValue = "-1")
    private int dayToRecall;//下一次recall的剩余时间
    @Column(nullable = false, defaultValue = "-1")
    private int assistTimes;//辅助recall的次数
    @Column(nullable = false, defaultValue = "-1")
    private int dayToAssist;//下次辅助recall的剩余时间
    @Column(nullable = false, unique = true)
    private String name;//任务名 保存的时候默认为分类名+ +任务名

    private Tag tag;//外键映射

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
*/
