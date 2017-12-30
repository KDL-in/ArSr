/*
package com.arsr.arsr.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by KundaLin on 17/12/29.
 *//*


public class Category extends DataSupport {
    private int id;
    @Column(unique = true, nullable = false)
    private String name;//任务名

    private List<Tag> tags =new ArrayList<>();//外键映射集合

    public List<Tag> getTags() {//延迟加载
        return DataSupport.where("category_id=?", String.valueOf(id)).find(Tag.class);
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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
}
*/
