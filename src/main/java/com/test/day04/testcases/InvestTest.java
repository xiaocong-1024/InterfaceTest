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
 * @date 2022/3/19 0019 - 11:30
 * 投资接口 -->依赖登录(投资人)
 * --> 依赖添加项目接口(loan_id) + 审核接口
 */
public class InvestTest extends BaseCase {
    private List<CaseInfo> cases;
    @BeforeClass
    public void setUpClass(){
        this.cases = getExcelDatas(Constants.EXCEL_PATH, 6);
        //参数化替换
        this.cases = paramsReplace(this.cases);
    }

    @Test(dataProvider = "getInvestDatas")
    public void testInvest(CaseInfo caseInfo){
        Map<String, Object> requestHeaderMap = fromJsonToMap(caseInfo.getRequestHeader());
//在每个请求发送之前 配置日志文件信息
        String logFilePath = addLogToFile(caseInfo.getModule(), caseInfo.getCaseId());
        Response res =
        given().log().all().
                headers(requestHeaderMap).
                body(caseInfo.getInputParams()).
        when().
                post(caseInfo.getUrl()).
        then().
                log().all().extract().response();

        if(!Constants.IS_DEBUG) {
            //在每个请求执行结束之后,将日志文件 添加到 allure报表中
            try {
                Allure.addAttachment("请求响应信息", new FileInputStream(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        assertExpected(caseInfo , res);

    }

    @DataProvider Object[] getInvestDatas(){
        return this.cases.toArray();
    }
}
