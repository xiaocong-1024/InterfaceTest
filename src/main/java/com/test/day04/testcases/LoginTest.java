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
 * @date 2022/3/16 0016 - 9:51
 * token鉴权
 */
public class LoginTest extends BaseCase {
    private List<CaseInfo> cases;

    //测试数据初始化(用例前置)
    @BeforeClass
    public void setUp(){
        //String excelPath = "src/main/resources/testcases_V1.xlsx";
        this.cases = getExcelDatas(Constants.EXCEL_PATH, 1);
        //进行参数化
        this.cases = paramsReplace(this.cases);
    }

    @Test(dataProvider = "getLoginDatas")
    public void testLogin(CaseInfo caseInfo) throws JsonProcessingException {
        //将请求头(json字符串)  转为    map对象
        Map<String, Object> headersMap = fromJsonToMap(caseInfo.getRequestHeader());
        //在每个请求发送之前 配置日志文件信息
        String logFilePath = addLogToFile(caseInfo.getModule(), caseInfo.getCaseId());
        Response res =
        given().log().all().
                headers(headersMap).
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

        assertExpected(caseInfo, res);

        //参数化(添加环境变量)
        // step1：:从响应结果中提取出getUserInfo接口需要字段并保存到环境变量中
        //如果在响应结果中  找到 依赖字段
        if(caseInfo.getCaseId() == 1){
            //直接接口执行成功了才会添加 环境变量
            //GlobalEnvironmenrtVarTest.memberId = memberId;
            GlobalEnvironmenrtVarTest.envVar.put("loan_token", res.path("data.token_info.token"));
        }else if(caseInfo.getCaseId() == 2){
            GlobalEnvironmenrtVarTest.envVar.put("invester_token", res.path("data.token_info.token"));
        }else if(caseInfo.getCaseId() == 3){
            GlobalEnvironmenrtVarTest.envVar.put("admin_token", res.path("data.token_info.token"));
        }
    }


    @DataProvider
    public Object[] getLoginDatas(){
        return this.cases.toArray();
    }


}
