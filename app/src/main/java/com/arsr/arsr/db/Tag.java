package com.arsr.arsr.db;

public class Tag extends Entity{
    private long id;
    private String name;//标签名 保存时为分类名+ +标签名
    private String prefix;//用于自动生成任务前缀
    private String note;//备注说明
    private long cid;//外键分类

    public Tag(long id, String name, String prefix, long cid, String note) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        this.note = note;
        this.cid = cid;
    }

    public Tag() {
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }
    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
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

    @Override
    public String toString() {
        return id+" | "+name+" | "+prefix+" | "+cid+" | "+note;
    }
}