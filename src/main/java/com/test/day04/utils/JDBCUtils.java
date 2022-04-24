package com.test.day04.utils;

import com.test.day04.data.Constants;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author xiaocong
 * @date 2022/3/18 0018 - 8:37
 * 定义一个  JDBC工具类
 * 对数据库进行操作 -->dbutils里面的核心类库QuaryRunner
 * 对数据库进行 增删改  查
 */
public class JDBCUtils {

    //返回一个数据库连接对象
    public static Connection getConnection(){
        //oracel: jdbc:oracel:thin:@localhost:1521:DBname
        //sqlserver: jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=dbname
        //mysql:jdbc:mysql://localhost:3306/DBname
        //返回一个数据库连接对象

        //String url = "jdbc:mysql://8.129.91.152:3306/futureloan?useUnicode=true&characterEncoding=utf-8";
        String url = "jdbc:mysql://" + Constants.DB_BASE_URL + "/" + Constants.DB_NAME  + "?useUnicode=true&characterEncoding=utf-8";
        System.out.println("mysql url:" + url);
        String user = Constants.DB_USERNAME;
        String pwd = Constants.DB_PASSWORD;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, pwd);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    /**
    * @Param sql语句
    * @Return void
    * 方法说明：可以对 数据库表数据进行 增 (insert) 删(delete)  改(update)
    */
    public static void update(String sql){
        //step1：获取数据库连接对象
        Connection conn = getConnection();
        //step2:创建 QueryRunner对象
        //dbutils核心功能类QueryRunner：提供了对sql语句操作的api
        QueryRunner qr = new QueryRunner();
        //step3：调用 操作sql语句的 api
        try {
            qr.update(conn, sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //step4:关闭连接
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //查询一条记录
    public static Map<String, Object> selectOne(String sql){
        //1.创建 数据库连接对象
        Connection conn = getConnection();
        //2.创建 QueryRunner操作sql语句对象
        QueryRunner qr = new QueryRunner();
        Map<String, Object> result = null;
        try {
            result = qr.query(conn, sql, new MapHandler());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    //查询多条数据 -->结果使用MapListHandler保存
    public static List<Map<String, Object>> selectAll(String sql){
        Connection conn = getConnection();
        QueryRunner qr = new QueryRunner();
        List<Map<String, Object>> result = null;
        try {
            result = qr.query(conn, sql, new MapListHandler());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }


    public static Object selectSingle(String sql){
        Connection conn = getConnection();
        QueryRunner qr = new QueryRunner();
        Object result = null;
        try {
            result = qr.query(conn,sql , new ScalarHandler());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }



    public static void main(String[] args){
        //1.QueryRunner核心功能类的 update操作
        //sql语句insert操作
        //String sql = "INSERT INTO member VALUES(10013,\"lemon\",\"25D55AD283AA400AF464C76D713C07AD\",\"13488328449\",1,\"0\",\"2022-03-18 00:09:03\");";
        //sql语句update操作
        //String sql = "update member set reg_name='lemon' where mobile_phone='13488328449';";
        //update(sql);

        //2.QueryRunner 核心功能类的  select操作
        // select操作的结果集会保存到
        // ResultSetHandler接口 的三个实现类：
        //2.1查询一条记录(保存到MapHandler对象中)
        //String sql = "select * from member where mobile_phone='13488328449';";
        //Map<String, Object> res = selectOne(sql);
        //System.out.println(res);
        //2.2 查询多条记录(保存到MapListHandler对象中)
        //String sql = "select * from member limit 5;";
        //List<Map<String, Object>> res = selectAll(sql);
        //System.out.println(res);
        //2.3 查询单个记录
        String sql = "select count(*) from member where mobile_phone='13488328440'";
        Object result = selectSingle(sql);
        if((Long)result == 1){
            System.out.println("手机号已被注册");
        }else{
            System.out.println("手机号没被注册");
        }
    }

}
