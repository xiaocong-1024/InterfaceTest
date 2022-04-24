package com.test.day04.base;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.day04.data.Constants;
import com.test.day04.data.GlobalEnvironmenrtVarTest;
import com.test.day04.pojo.CaseInfo;
import com.test.day04.utils.JDBCUtils;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;


/**
 * @author xiaocong
 * @date 2022/3/18 0018 - 10:54
 * 提取 所有测试方法中的  公共方法
 */
public class BaseCase {

    //设置Rest-Assured的全局配置
    @BeforeTest
    public void setUpTest() {
        //作用：Rest-Assured框架 接收的响应数据为json数据类型，并且该json中包含小数，使用Gpath表达式提取小数时 使用BIG_DECIMAL来存储
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //配置项目地址,后续的所有接口请求都会自动在 接口地址前面拼接上该项目地址
        RestAssured.baseURI = Constants.BASE_URL;
    }


    /**
    * @Param interfaceName:接口名称  caseId:测试用例id
    * @Return 该条测试用例 写入的日志文件路径
    *
    */
    public String addLogToFile(String interfaceName, Integer caseId) {
        //如果是 调试状态,日志直接输出到控制台
        if (!Constants.IS_DEBUG) {
            String fileDir = "target/log/" + interfaceName;
            File logFileDir = new File(fileDir);
            if (!logFileDir.isDirectory()) {
                //连续创建层级目录
                logFileDir.mkdirs();
            }
            String logFilePath = fileDir + "/" + interfaceName + "_" + caseId + ".log";
            PrintStream fileOutputSream = null;
            try {
                fileOutputSream = new PrintStream(new File(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //在每个请求之前  添加该配置(是将控制台输出的 请求/响应信息 重定向到指定日志文件中)
            RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutputSream));
            return logFilePath;
        }
        return null;
    }


    //将json数据  转为  map类型
    /**
    * @Param jsonStr:要转换的json字符串
    * @Return 转换后的map
    */
    public Map<String, Object> fromJsonToMap(String jsonStr){
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> headersMap = null;
        try {
            headersMap = mapper.readValue(jsonStr, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return headersMap;
    }


    //断言设置：
    /**
    * @Param caseInfo:一条测试用例数据  res：请求的响应结果
    * @Return void
    * 断言：一般数据类型  和  小数数据类型(Float或Double)
    */
    public void assertExpected(CaseInfo caseInfo, Response res){
        ObjectMapper mapper = new ObjectMapper();
        //断言设置
        Map<String, Object> expectedMap = null;
        try {
            expectedMap = mapper.readValue(caseInfo.getExpected(), Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Set<Map.Entry<String, Object>> entries = expectedMap.entrySet();
        for(Map.Entry<String, Object> entry : entries){
            //实际值   期望值
            //step1：配置json中小数数值的存储类型
            //step2：将期望结果转化为BigDecimal类型
            Object expected = entry.getValue();
            //Assert.assertEquals(actualValue, expectedValue);
            if(expected instanceof Float || expected instanceof Double){
                //需要将期望结果 转换为 BigDecimal
                BigDecimal expectedValue = new BigDecimal(expected.toString());
                Assert.assertEquals(res.path(entry.getKey()), expectedValue, "响应结果断言失败");
            }else{
                //期望结果不是 小数类型的
                Assert.assertEquals(res.path(entry.getKey()), entry.getValue(),"响应结果断言失败");
            }
        }
    }


    //断言数据库
    /**
    * @Param  caseInfo:一条测试数据
    * @Return
    */
    public void assertSql(CaseInfo caseInfo){
        String checkSql = caseInfo.getCheckSql();
        if(checkSql != null){
            //从数据库查询
            Map<String, Object> checkSqlMap = fromJsonToMap(checkSql);
            Set<Map.Entry<String, Object>> entries = checkSqlMap.entrySet();
            for(Map.Entry<String, Object> entry : entries){
                String sql = entry.getKey();
                Object actualValue = JDBCUtils.selectSingle(sql);
                //System.out.println("实际值原生的数据类型::" + actualValue.getClass());
                Object expectedValue = entry.getValue();
                //System.out.println("期望值原生的数据类型::" + expectedValue.getClass());
                if(expectedValue instanceof Double){
                    //如果期望值为 Double类型，需要将其转化为BigDecimal
                    BigDecimal expected = new BigDecimal(expectedValue.toString());
                    //System.out.println("数据库期望值数据类型为---" + expected.getClass());
                    //System.out.println("数据库实际值数据类型为---" + actualValue.getClass());
                    Assert.assertEquals(actualValue, expected,"数据库断言失败");
                }else if(expectedValue instanceof Integer){
                    //如果期望值为Integer类型,需要将其转为Long
                    Long expected = new Long(expectedValue.toString());
                    Assert.assertEquals(actualValue, expected,"数据库断言失败");
                }else{
                    //其他类型断言
                    Assert.assertEquals(actualValue, expectedValue,"数据库断言失败");
                }
            }
        }
    }


    /**
     * @Param caseInfo:一条测试数据
     * @Return 参数化后的 测试数据
     *
     */
    public CaseInfo paramsReplaceCaseInfo(CaseInfo caseInfo){
        //需要进行参数化的：请求头  接口地址  输入参数  期望返回结果
            String requestHeaders = caseInfo.getRequestHeader();
            //step1：先进行参数化替换
            requestHeaders = regexReplace(caseInfo.getRequestHeader());
            //step2:用参数化替换后的 字符串 替换 测试用例的 相应部分
            caseInfo.setRequestHeader(requestHeaders);

            String url = caseInfo.getUrl();
            url = regexReplace(url);
            caseInfo.setUrl(url);

            String inputParams = caseInfo.getInputParams();
            inputParams = regexReplace(inputParams);
            caseInfo.setInputParams(inputParams);

            String expected = caseInfo.getExpected();
            expected = regexReplace(expected);
            caseInfo.setExpected(expected);

            String checkSql = caseInfo.getCheckSql();
            checkSql = regexReplace(checkSql);
            caseInfo.setCheckSql(checkSql);
            return caseInfo;
    }


    /**
     * @Param caseInfoList:从excel中读取到的所有测试数据
     * @Return 参数化后的 测试数据列表
     *
     */
    public List<CaseInfo> paramsReplace(List<CaseInfo> caseInfoList){
        //需要进行参数化的：请求头  接口地址  输入参数  期望返回结果
        for(CaseInfo caseInfo : caseInfoList){
            String requestHeaders = caseInfo.getRequestHeader();
                //step1：先进行参数化替换
            requestHeaders = regexReplace(caseInfo.getRequestHeader());
            //step2:用参数化替换后的 字符串 替换 测试用例的 相应部分
            caseInfo.setRequestHeader(requestHeaders);

            String url = caseInfo.getUrl();
            url = regexReplace(url);
            caseInfo.setUrl(url);

            String inputParams = caseInfo.getInputParams();
            inputParams = regexReplace(inputParams);
            caseInfo.setInputParams(inputParams);

            String expected = caseInfo.getExpected();
            expected = regexReplace(expected);
            caseInfo.setExpected(expected);

            String checkSql = caseInfo.getCheckSql();
            checkSql = regexReplace(checkSql);
            caseInfo.setCheckSql(checkSql);
        }
        return caseInfoList;
    }

    /**
     * @Param
     * sourceContent:要进行 参数化替换的 原字符串
     * newContent:替换的值(这里指的是  环境变量中保存的值)
     * @Return
     *替换后的字符串
     */
    public static String regexReplace(String sourceContent){
        if(sourceContent == null){
            return sourceContent;
        }
        //step1：使用正则表达式 找到 要替换的  子串
        // 1.1 创建一个模式对象(参数  需 匹配表达式)
        //String regStr = "\\{\\{(.+)\\}\\}";
        String regStr = "\\{\\{([a-z A-Z 0-9 _]+)\\}\\}";
        Pattern pattern = Pattern.compile(regStr);
        //1.2 创建一个匹配器(符合  正则表达式规则的  匹配器),参数为 匹配字符串
        Matcher matcher = pattern.matcher(sourceContent);
        //1.3 进行匹配(循环匹配,没有匹配到会返回false)
        String findStr = "";
        String envKey = "";
        while(matcher.find()){
            //匹配到的 完整字符串---->作用：要被替换掉
            //System.out.println(matcher.group(0));
            //step2: 将匹配到的值 进行  替换操作(使用环境变量保存的)
            findStr = matcher.group(0);
            //匹配到的  第一个分组字符串 ---->作用：环境变量里的key,可以取出对应的环境变量值
            envKey = matcher.group(1);
            //System.out.println(matcher.group(1));
            //找到一个{{}} 进行替换
            sourceContent = sourceContent.replace(findStr, GlobalEnvironmenrtVarTest.envVar.get(envKey) + "");
            //System.out.println("将" + findStr + "替换为" + GlobalEnvironmenrtVarTest.envVar.get(envKey));
            //System.out.println(sourceContent);
        }
        return sourceContent;
    }

    //包装读取excel文件
    /**
     *参数：
     *  excelPath:excel文件路径
     * sheetIndex：开始读取的sheet索引
     * 返回值：从excel文件中读取的所有测试数据
     */
    public List<CaseInfo> getExcelDatas(String excelPath, Integer sheetIndex){
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(sheetIndex);

        File excelFile = new File(excelPath);
        return ExcelImportUtil.importExcel(excelFile, CaseInfo.class, importParams);
    }

}
