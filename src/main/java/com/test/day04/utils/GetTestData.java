package com.test.day04.utils;

import com.github.javafaker.Faker;

import java.util.Locale;
import java.util.Random;

/**
 * @author xiaocong
 * @date 2022/3/18 0018 - 11:04
 * 数据源的获取
 */
public class GetTestData {
    public static String getNotRegisteredMobilePhone(){
        String mobilePhone = null;
        while(true){
            //step1:获取一个手机号
            Faker faker = new Faker(Locale.CHINA);
            mobilePhone = faker.phoneNumber().cellPhone();
            //step2:判断该手机号是否被注册(查询数据库)
            String sql = "select count(*) from member where mobile_phone=" + mobilePhone;
            Object result = JDBCUtils.selectSingle(sql);
            if((Long)result == 0){
                //如果手机号未被注册,则退出返回手机号
                break;
            }
        }
        return mobilePhone;
    }

    public static String getRandomPhoneNum() {
        String mobilePhone = "134";
        while (true) {
            for (int i = 0; i < 8; i++) {
                Random random = new Random();
                Integer value = random.nextInt(9);
                //System.out.println(value);
                mobilePhone += value;
            }

            //step2:判断该手机号是否被注册(查询数据库)
            String sql = "select count(*) from member where mobile_phone=" + mobilePhone;
            Object result = JDBCUtils.selectSingle(sql);
            if ((Long) result == 0) {
                //如果手机号未被注册,则退出返回手机号
                break;
            }
        }
        return mobilePhone;
    }


    //public static void main(String[] args){
    //    System.out.println(getRandomPhoneNum());
    //}
}
