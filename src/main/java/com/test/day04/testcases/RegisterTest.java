package com.test.day04.testcases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.day04.base.BaseCase;
import com.test.day04.data.Constants;
import com.test.day04.data.GlobalEnvironmenrtVarTest;
import com.test.day04.pojo.CaseInfo;
import com.test.day04.utils.GetTestData;
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
 * @date 2022/3/16 0016 - 15:40
 * 注册 ->登录 ->获取用户登录信息
 */
public class RegisterTest extends BaseCase {
    private List<CaseInfo> cases;

    @BeforeClass
    public void setUpClass(){
        this.cases = getExcelDatas(Constants.EXCEL_PATH, 0);
    }


    @Test(dataProvider = "getRegisterDatas")
    public void Register01(CaseInfo caseInfo) throws JsonProcessingException {
        //step1：将所需的参数写入环境变量
        if(caseInfo.getCaseId() == 1){
            //GlobalEnvironmenrtVarTest.envVar.put("loan_mobile_phone",GetTestData.getNotRegisteredMobilePhone());
            GlobalEnvironmenrtVarTest.envVar.put("loan_mobile_phone", GetTestData.getRandomPhoneNum());
        }else if(caseInfo.getCaseId() == 2){
            //GlobalEnvironmenrtVarTest.envVar.put("invester_mobile_phone",GetTestData.getNotRegisteredMobilePhone());
            GlobalEnvironmenrtVarTest.envVar.put("invester_mobile_phone", GetTestData.getRandomPhoneNum());
        }else if(caseInfo.getCaseId() == 3){
            //GlobalEnvironmenrtVarTest.envVar.put("admin_mobile_phone",GetTestData.getNotRegisteredMobilePhone());
            GlobalEnvironmenrtVarTest.envVar.put("admin_mobile_phone", GetTestData.getRandomPhoneNum());
        }else if(caseInfo.getCaseId() == 4){
            //GlobalEnvironmenrtVarTest.envVar.put("test_mobile_phone",GetTestData.getNotRegisteredMobilePhone());
            GlobalEnvironmenrtVarTest.envVar.put("test_mobile_phone", GetTestData.getRandomPhoneNum());
        }
        //step2：对该条 测试数据 进行参数化替换
        caseInfo = paramsReplaceCaseInfo(caseInfo);
        Map<String, Object> headersMap = fromJsonToMap(caseInfo.getRequestHeader());

        //在每个请求发送之前 配置日志文件信息
        String logFilePath = addLogToFile(caseInfo.getModule(), caseInfo.getCaseId());
        Response res =
        given().
                log().all().
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
        //断言：
        //响应断言
        assertExpected(caseInfo, res);

        //数据库断言
        assertSql(caseInfo);

        Map<String, Object> inputParams = fromJsonToMap(caseInfo.getInputParams());

        //注册成功的手机号  密码  member_id都保存到环境变量中
        if(caseInfo.getCaseId() == 1){
            GlobalEnvironmenrtVarTest.envVar.put("loan_pwd", inputParams.get("pwd"));
            GlobalEnvironmenrtVarTest.envVar.put("loan_member_id",res.path("data.id") + "");
        }else if(caseInfo.getCaseId() == 2){
            GlobalEnvironmenrtVarTest.envVar.put("invester_pwd", inputParams.get("pwd"));
            GlobalEnvironmenrtVarTest.envVar.put("invester_member_id", res.path("data.id") + "");
        }else if(caseInfo.getCaseId() == 3){
            GlobalEnvironmenrtVarTest.envVar.put("admin_pwd", inputParams.get("pwd"));
            GlobalEnvironmenrtVarTest.envVar.put("admin_member_id", res.path("data.id") + "");
        }
    }

    @DataProvider
    public Object[] getRegisterDatas(){
        return cases.toArray();
    }


}
