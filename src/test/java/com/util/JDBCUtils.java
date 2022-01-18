package com.util;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JDBCUtils {
    public static Connection getConnection() {
        //定义数据库连接
        //Oracle：jdbc:oracle:thin:@localhost:1521:DBName
        //SqlServer：jdbc:microsoft:sqlserver://localhost:1433; DatabaseName=DBName
        //MySql：jdbc:mysql://localhost:3306/DBName
        //端口不写默认3306
        String url="jdbc:mysql://mall.lemonban.com/yami_shops?useUnicode=true&characterEncoding=utf-8";
        String user="lemon";
        String password="lemon123";
        //定义数据库连接对象
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user,password);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    @Test
    public static void test1() throws SQLException {
        //0、创建数据库连接
        Connection conn= getConnection();
        //1、生成QueryRunner对象SELECT mobile_code FROM tz_sms_log WHERE user_id='13642308712' ORDER BY rec_date DESC LIMIT 1
        QueryRunner qr = new QueryRunner();
        //2、调用query方法来实现查询操作
        //2-1、多条结果集
//        String sql="SELECT * FROM tz_sms_log ";
//        List<Map<String, Object>> datas = qr.query(conn, sql, new MapListHandler());
//        System.out.println(datas.get(0).get("mobile_code"));
        //2-2、一条结果集
//        String sql="SELECT * FROM tz_sms_log WHERE user_id='13642308712'";
//        Map<String, Object> datas = qr.query(conn, sql, new MapHandler());
//        System.out.println(datas);
        //2-3、单个数据
//        String sql="SELECT mobile_code FROM tz_sms_log WHERE user_id='13642308712' ORDER BY rec_date DESC LIMIT 1";
//        String code = qr.query(conn, sql, new ScalarHandler<>());
//        System.out.println(code);
    }

    //封装查询单个数据
    public static Object quereySingleData(String sql)  {
        //参数化替换
        sql=Environment.replaceParams(sql);
        //0、创建数据库连接
        Connection conn= getConnection();
        //1、生成QueryRunner对象
        QueryRunner qr = new QueryRunner();
        Object data =null;
        //2-3、单个数据
        try {
            data = qr.query(conn, sql, new ScalarHandler<>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return data;
    }

    //封装查询一条数据
    public static Map<String,Object> quereyOneData(String sql)  {
        //参数化替换
        sql=Environment.replaceParams(sql);
        //0、创建数据库连接
        Connection conn= getConnection();
        //1、生成QueryRunner对象
        QueryRunner qr = new QueryRunner();
        Map<String,Object> data =null;
        //2-3、单个数据
        try {
            data = qr.query(conn, sql, new MapHandler());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return data;
    }
}