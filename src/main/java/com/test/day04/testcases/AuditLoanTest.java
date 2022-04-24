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
 * @date 2022/3/19 0019 - 9:10
 * 审核项目
 * 依赖接口 -->登录接口(管理员登录)
 * 依赖接口 -->添加项目接口
 */
public class AuditLoanTest extends BaseCase {
    private List<CaseInfo> cases;

    @BeforeClass
    public void setUpClass(){
        this.cases = getExcelDatas(Constants.EXCEL_PATH, 5);
        //参数化替换
        this.cases = paramsReplace(this.cases);
    }

    @Test(dataProvider = "getAuditLoanDatas")
    public void auditLoanTest(CaseInfo caseInfo){
        Map<String, Object> requestHeaderMap = fromJsonToMap(caseInfo.getRequestHeader());
        //在每个请求发送之前 配置日志文件信息
        String logFilePath = addLogToFile(caseInfo.getModule(), caseInfo.getCaseId());
        Response res =
        given().
                log().all().
                headers(requestHeaderMap).
                body(caseInfo.getInputParams()).
        when().
                patch(caseInfo.getUrl()).
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
    public Object[] getAuditLoanDatas(){
        return this.cases.toArray();
    }

}
