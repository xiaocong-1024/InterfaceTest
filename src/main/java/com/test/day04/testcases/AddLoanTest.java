package com.test.day04.testcases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.day04.base.BaseCase;
import com.test.day04.data.Constants;
import com.test.day04.data.GlobalEnvironmenrtVarTest;
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
 * @date 2022/3/19 0019 - 9:09
 * 添加项目、
 * 依赖接口：注册接口-->loan_member_id
 * 登录接口 --->loan_token
 */
public class AddLoanTest extends BaseCase {
    private List<CaseInfo> cases;

    @BeforeClass
    public void setUpClass(){
       //读取excel数据
        this.cases = getExcelDatas(Constants.EXCEL_PATH, 4);
        //进行参数化替换
        this.cases = paramsReplace(this.cases);
    }

    @Test(dataProvider = "getAddLoanDatas")
    public void addLoanTest(CaseInfo caseInfo) throws JsonProcessingException {
        //System.out.println("测试数据::" + caseInfo);
        Map<String, Object> requestHeaderMap = fromJsonToMap(caseInfo.getRequestHeader());
        //在每个请求发送之前 配置日志文件信息
        String logFilePath = addLogToFile(caseInfo.getModule(), caseInfo.getCaseId());
        Response res =
        given().
                log().all().
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

        //断言：
        assertExpected(caseInfo, res);
        //参数化设置(环境变量)
        //审核项目接口  -->依赖该接口返回值
        if(caseInfo.getCaseId() == 1){
            //Integer loan_id = res.path("data.id"
            GlobalEnvironmenrtVarTest.envVar.put("loan_id", res.path("data.id"));
        }else if(caseInfo.getCaseId() == 2){
            //Integer loan_test_id = res.path("data.id")
            GlobalEnvironmenrtVarTest.envVar.put("loan_test_id", res.path("data.id"));
        }
    }


    @DataProvider
    public Object[] getAddLoanDatas() {
        return this.cases.toArray();

    }
}
