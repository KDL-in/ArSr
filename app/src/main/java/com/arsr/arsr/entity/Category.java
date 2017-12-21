package com.arsr.arsr.entity;

import java.util.List;

/**
 * 分类实体类，用于存放类别信息，持有类别下子任务集的信息
 * Created by KundaLin on 17/12/21.
 */

public class Category {
    private String name;
    private List<Task> children;

    public List<Task> getChildren() {
        return children;
    }

    public void setChildren(List<Task> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
