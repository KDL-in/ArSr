package com.arsr.arsr.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by KundaLin on 17/12/29.
 */

public class Category extends DataSupport {
    private int id;
    @Column(unique = true, nullable = false)
    private String name;//任务名

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
