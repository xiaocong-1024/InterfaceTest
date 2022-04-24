package com.test.day04.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaocong
 * @date 2022/3/16 0016 - 19:33
 * //环境变量类(都是类属性)
 */
public class GlobalEnvironmenrtVarTest {
    //存储用户id
    //public static Integer memberId;
    //存储环境变量
    //key --->引用参数({{key}})的key
    public static Map<String, Object> envVar = new HashMap<String, Object>();
}
