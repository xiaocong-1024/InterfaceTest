package com.test.practice;

import java.util.HashMap;

/**
 * @author xiaocong
 * @date 2022/4/2 0002 - 16:29
 */
public class HashMapTest {
    public static void main(String[] args){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name","xiaoke");
        map.put("age",12);
        map.put("gender", "men");
        //System.out.println();
        System.out.println(map.get("name"));
        System.out.println(map.get("a"));
    }
}
