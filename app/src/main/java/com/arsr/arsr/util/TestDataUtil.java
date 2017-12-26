package com.arsr.arsr.util;

import com.arsr.arsr.entity.Child;
import com.arsr.arsr.entity.Group;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 测试用数据生成类
 * Created by KundaLin on 17/12/26.
 */

public class TestDataUtil {
    //临时数据
    private static String[] groupNames = {"CET-6", "考研", "Math", "PcNet"};
    private static String[][] childNames = {
            {"List 1", "List 2", "List 3", "List 3"},
            {"L 1", "L 2", "L 3", "L 4"},
            {"Chapter 1", "Chapter 2", "Chapter 3", "Chapter 4", "Chapter 5"},
            {"c1", "c2", "c3", "c4"}
    };

    public static List<Group> getGroups() {
        List<Group> groups = new ArrayList<>();
        //写入数据
        for (String name :
                groupNames) {
            Group group = new Group();
            group.setName(name);  //写入名字数据
            groups.add(group);
        }
        return groups;
    }

    public static List<List<Child>> getChildren() {
        List<List<Child>> children = new ArrayList<>();
        //写入数据
        for (int i = 0; i < childNames.length; i++) {
            List<Child> childrenInGroup = new ArrayList<>();
            for (int j = 0; j < childNames[i].length; j++) {
                Child child = new Child();
                child.setName(childNames[i][j]); //写入名字
                childrenInGroup.add(child);
            }
            children.add(childrenInGroup);
        }
        return children;
    }

    public static List<String> getGroupNames() {
        return Arrays.asList(groupNames);
    }
}
