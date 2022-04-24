package com.test.day04.testcases;

import com.fasterxml.jackson.core.JsonProcessingException;
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
 * @date 2022/3/16 0016 - 19:24
 * //获取用户信息接口测试用例
 */
public class GetUserInfoTest extends BaseCase {
    private List<CaseInfo> cases;
    //测试数据初始化(用例前置)
    //当前类中所有的测试方法执行之前执行的方法
    @BeforeClass
    public void setUp(){
        //String excelPath = "src/main/resources/testcases_V1.xlsx";
        this.cases = getExcelDatas(Constants.EXCEL_PATH, 2);
        //需要对  从excel文件中读取的数据进行 参数化替换
        this.cases = paramsReplace(this.cases);
    }

    @Test(dataProvider = "getUserInfoDatas")
    public void getUserInfoTest01(CaseInfo caseInfo) throws JsonProcessingException {
        //将请求头信息 转为 map
        Map<String, Object> requestHeaderMap = fromJsonToMap(caseInfo.getRequestHeader());
        //在每个请求发送之前 配置日志文件信息
        String logFilePath = addLogToFile(caseInfo.getModule(), caseInfo.getCaseId());
        Response res =
        given().log().all().
                headers(requestHeaderMap).
        when().
                get(caseInfo.getUrl()).
        then().log().all().extract().response();

        if(!Constants.IS_DEBUG) {
            //在每个请求执行结束之后,将日志文件 添加到 allure报表中
            try {
                Allure.addAttachment("请求响应信息", new FileInputStream(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        assertExpected(caseInfo, res);
    }


    @DataProvider
    public Object[] getUserInfoDatas(){
        return this.cases.toArray();
    }

}
