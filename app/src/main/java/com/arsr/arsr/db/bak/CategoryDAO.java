/*
package com.arsr.arsr.dao;

import com.arsr.arsr.db.Category;
import com.arsr.arsr.util.LogUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

*/
/**
 * Created by KundaLin on 17/12/30.
 *//*


public class CategoryDAO {
    */
/**
     * 插入新类别
     * @param categoryName
     *//*

    public static void insert(String categoryName) {
        Category category = new Category();
        category.setName(categoryName);
        category.setId(4);
        category.save();
    }

    public static void output(List<Category> categories) {
        LogUtil.d("IN Category");
        for (Category category
                : categories
                ) {
            LogUtil.d(category.getId() + " | " + category.getName());
        }
    }


    */
/**
     * 查找所有类别
     *
     * @return 类别列表
     *//*

    public static List<Category> selectAll() {
        return DataSupport.findAll(Category.class);
    }

    */
/**
     * 删除名字等于category的数据
     * @param category
     *//*

    public static void delete(String category) {
        DataSupport.deleteAll(Category.class, "name=?", category);
    }

    */
/**
     * 通过类名查找类id
     * @param name 分类名
     * @return
     *//*

    public static int selectId(String name) {
        return DataSupport.where("name=?", name).findFirst(Category.class).getId();
    }

    public static Category select(String name) {
        return DataSupport.where("name=?", name).findFirst(Category.class);
    }
}
*/
