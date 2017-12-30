/*
package com.arsr.arsr.dao;

import com.arsr.arsr.db.Category;
import com.arsr.arsr.db.Tag;
import com.arsr.arsr.util.LogUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

*/
/**
 * Created by KundaLin on 17/12/30.
 *//*


public class TagDAO {
    */
/**
     * 插入新标签
     *
     * @param name   标签名
     * @param prefix 用于自动生成名字前缀
     * @param cName  列别名
     * @param note   备注 可以为空
     *//*

    public static void insert(String name, String prefix, String cName, String note) {
//        Tag tag = new Tag();
//        tag.setName(cName + "_" + name);
//        tag.setPrefix(prefix);
//        tag.setcId(CategoryDAO.selectId(cName));
//        tag.setNote(note);
//        tag.save();
        Tag tag = new Tag();
        tag.setName(cName+"_"+name);
        tag.setPrefix(prefix);
        tag.setNote(note);
        Category category = CategoryDAO.select(cName);
        tag.setCategory(category);
        tag.save();//持久化
        //建立外键联系
        category.getTags().add(tag);
        category.save();

    }

    */
/**
     * 查找所有标签
     *//*

    public static List<Tag> selectAll() {
        return DataSupport.findAll(Tag.class,true);

    }

    public static void output(List<Tag> tags) {
        LogUtil.d("IN Tag");
        for (Tag t :
                tags) {
            LogUtil.d(t.getId() + " | " + t.getName() + " | " + t.getPrefix() + " | " + " | " + t.getNote());
        }
    }
}
*/
