package com.arsr.arsr.db;

public class Category extends Entity {
    private long id;

    public Category() {
    }

    public Category(long id, String name) {

        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return id + " | " + name;
    }

    private String name;//任务名

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
}