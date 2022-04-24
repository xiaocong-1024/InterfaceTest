package com.test.day04.testcases;

import com.test.day04.base.BaseCase;
import com.test.day04.data.Constants;
import com.test.day04.pojo.CaseInfo;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * @author xiaocong
 * @date 2022/3/18 0018 - 21:56
 */
public class RechargeTest extends BaseCase {
    List<CaseInfo> cases;


    @BeforeClass
    public void setUpClass(){
        //测试前置：准备测试数据
        this.cases = getExcelDatas(Constants.EXCEL_PATH, 3);
        //需要进行参数化替换
        this.cases = paramsReplace(this.cases);
    }

    @Test(dataProvider = "getRechargeDatas")
    public void rechargeTest(CaseInfo caseInfo){
        Map<String, Object> requestHeaderMap = fromJsonToMap(caseInfo.getRequestHeader());
        //发送请求
        //在每个请求发送之前 配置日志文件信息
        String logFilePath = addLogToFile(caseInfo.getModule(), caseInfo.getCaseId());
        Response res =
        given().log().all().
                //对json响应数据 中的小数 使用BigDecimal来存储
                headers(requestHeaderMap).
                body(caseInfo.getInputParams()).
        when().
                post( caseInfo.getUrl()).
        then().log().all().extract().response();
        if(!Constants.IS_DEBUG) {
            //在每个请求执行结束之后,将日志文件 添加到 allure报表中
            try {
                Allure.addAttachment("请求响应信息", new FileInputStream(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

       assertExpected(caseInfo,res);
        assertSql(caseInfo);
        //参数化设置(接口依赖 响应字段)
        //投资人 账户的leave_amount --->投资接口需要
    }

    @DataProvider
    public Object[] getRechargeDatas(){
        return this.cases.toArray();
    }

}
