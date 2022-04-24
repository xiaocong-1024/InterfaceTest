package com.test.day04.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @author xiaocong
 * @date 2022/3/16 0016 - 9:31
 * excel文件里面的每一行数据映射的实体类
 * 使用@Excel注解将实体类的属性 和 excel文件的表头相关联
 */
public class CaseInfo {
    //序号(caseId)
    @Excel(name = "序号(caseId)")
    private Integer caseId;
    //接口模块(module)
    @Excel(name = "接口模块(module)")
    private String module;
    //用例标题(title)
    @Excel(name = "用例标题(title)")
    private String title;
    //请求头(requestHeader)
    @Excel(name = "请求头(requestHeader)")
    private String requestHeader;
    //请求方式(method)
    @Excel(name = "请求方式(method)")
    private String method;
    //接口地址(url)
    @Excel(name = "接口地址(url)")
    private String url;
    //参数输入(inputParams)
    @Excel(name = "参数输入(inputParams)")
    private String inputParams;
    //期望返回结果(expected)
    @Excel(name = "期望返回结果(expected)")
    private String expected;

    @Excel(name = "数据库校验(checkSQL)")
    private String checkSql;

    @Override
    public String toString() {
        return "CaseInfo{" +
                "caseId=" + caseId +
                ", module='" + module + '\'' +
                ", title='" + title + '\'' +
                ", requestHeader='" + requestHeader + '\'' +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", inputParams='" + inputParams + '\'' +
                ", expected='" + expected + '\'' +
                ", checkSql='" + checkSql + '\'' +
                '}';
    }

    public String getCheckSql() {
        return checkSql;
    }

    public void setCheckSql(String checkSql) {
        this.checkSql = checkSql;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInputParams() {
        return inputParams;
    }

    public void setInputParams(String inputParams) {
        this.inputParams = inputParams;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }
}
