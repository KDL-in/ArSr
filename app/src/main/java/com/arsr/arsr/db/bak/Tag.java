/*
package com.arsr.arsr.db;

import com.arsr.arsr.dao.TagDAO;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by KundaLin on 17/12/29.
 *//*


public class Tag extends DataSupport{
    private int id;
    @Column(nullable = false, unique = true)
    private String name;//标签名 保存时为分类名+ +标签名
    @Column(nullable = false)
    private String prefix;//用于自动生成任务前缀
    private String note;//备注说明

    private Category category;//外键映射
    private List<Task> tasks = new ArrayList<>();//外键映射

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Task> getTasks() {
        return DataSupport.where("tag_id=?",String.valueOf(id)).find(Task.class);
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
*/
