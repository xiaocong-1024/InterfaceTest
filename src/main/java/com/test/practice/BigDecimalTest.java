package com.test.practice;

import org.testng.Assert;

import java.math.BigDecimal;

/**
 * @author xiaocong
 * @date 2022/3/18 0018 - 23:41
 */
public class BigDecimalTest {
    public static void main(String[] args) {
        //Float aFloat = 0.01f;
        //Double bDouble = 0.01;
        //BigDecimal aBigDecimal = new BigDecimal(aFloat.toString());
        //BigDecimal bBigDecimal = new BigDecimal(bDouble.toString());
        //Assert.assertEquals(aBigDecimal, bBigDecimal);
        Integer aInt = 10;
        Long bLong = 10L;
        //将Integer 转换为 Long类型的
        Long aLong = Long.valueOf(aInt);
        Assert.assertEquals(aLong, bLong);
    }
}
