package com.test.practice;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @author xiaocong
 * @date 2022/4/2 0002 - 11:03
 */
public class JsonTest {
    public static void main(String[] args){
        //使用阿里的 fastjson库
    //    step1：导入依赖坐标

        //1.1  将json数据格式  转为  对象（map对象/自定义对象）
        //转为map对象方法1：
        //JSONObject object = (JSONObject)JSONObject.parse(jsonStr0); --->JSONObject.parse()返回值类型本质为JSONObject
        //Map<String, Object> map01 = (Map<String, Object>)object; --->JSONObject类  实现了 Map接口
        //上面两行代码可以简化为下面的一行：
        //Map<String, Object> map01= (Map<String, Object>)JSONObject.parse(jsonStr0);
        //System.out.println(map01);


        //方法：JSONObject.toJSONString(对象/数组);
        String jsonStr0 = "{\"mobile_phone\": \"13888888802\",\"pwd\": \"12345678\"}";
        Map<String, Object> map02 = JSONObject.parseObject(jsonStr0,Map.class);
        //---------------------------map对象转为json：
        String jsonStr01 = JSONObject.toJSONString(map02);
        System.out.println(map02);

        String jsonStr1 = "{\"name\":\"jack\",\"age\":18,\"gender\":\"man\",\"school\":\"刘集高中\"}";
        Student stu = JSONObject.parseObject(jsonStr1, Student.class);
        //自定义对象转为json:
        String jsonStr02 = JSONObject.toJSONString(stu);
        System.out.println(jsonStr02);


        String jsonArray02 = "[\n" +
                "\t{\"name\":\"jack\",\"age\":18,\"gender\":\"man\",\"school\":\"刘集高中\"},\n" +
                "\t{\"name\":\"rose\",\"age\":29,\"gender\":\"women\",\"school\":\"宜山高中\"},\n" +
                "\t{\"name\":\"happy\",\"age\":10,\"gender\":\"man\",\"school\":\"蓝光高中\"},\n" +
                "\t{\"name\":\"lili\",\"age\":38,\"gender\":\"women\",\"school\":\"新城高中\"}\n" +
                "]";
        List<Student> list02 = JSONObject.parseArray(jsonArray02, Student.class);
        //System.out.println(list02);
        //------------------------List<Student> 转为 json数组
        String jsonArr02 = JSONObject.toJSONString(list02);
        System.out.println(jsonArr02);



        String jsonArray01="[{\"mobile_phone\":\"13888888802\",\"pwd\":\"123456\"}," +
                "{\"mobile_phone\":\"13888888803\",\"pwd\":\"123456\"}," +
                "{\"mobile_phone\":\"13888888804\",\"pwd\":\"123456\"}" +
                "]";
        //-----------------转为List<Map>
        List<Map> list01 = JSONObject.parseArray(jsonArray01, Map.class);
        //将json数组   转为   list
        String jsonArr01 = JSONObject.toJSONString(list01);
        System.out.println(jsonArray01);

    }
}
