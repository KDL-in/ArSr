package com.arsr.arsr.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by KundaLin on 17/12/29.
 */

public class Tag extends DataSupport{
    private int id;
    private int cId;//分类id
    @Column(nullable = false, unique = true)
    private String name;//标签名 保存时为分类名+ +标签名
    @Column(nullable = false)
    private String prefix;//用于自动生成任务前缀
    private String note;//备注说明

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
