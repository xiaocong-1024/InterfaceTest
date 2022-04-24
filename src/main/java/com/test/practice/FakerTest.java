package com.test.practice;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;

import java.util.Locale;

/**
 * @author xiaocong
 * @date 2022/3/18 0018 - 10:36
 * 伪造测试数据
 */
public class FakerTest {
    public static void main(String[] args){
        //1.默认伪造英文数据
        //Faker faker = new Faker();
        //
        ////随机生成名字
        //String name = faker.name().fullName();
        //System.out.println(name);
        //String firstName = faker.name().firstName();
        //System.out.println(firstName);
        //String lastName = faker.name().lastName();
        //System.out.println(lastName);

        //2.伪造中文数据
        Faker faker = new Faker(Locale.CHINA);
        //2.1 名字
        String name = faker.name().fullName();
        System.out.println("name::" + name);
        //2.2 手机号
        String mobilePhone = faker.phoneNumber().cellPhone();
        System.out.println("手机号::" + mobilePhone);

        //2.3 地址
        String address = faker.address().fullAddress();
        System.out.println("地址：：" + address);
    }
}
