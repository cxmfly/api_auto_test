package com.common;

import com.alibaba.fastjson.JSONObject;
import com.util.JDBCUtils;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.Map;
import java.util.Set;

public class BaseTest {
    //通用的响应断言方法
    //assertDatas 是Excel中的断言设计，json格式设计
    public void assertResponse(String assertDatas, Response res){
        Map<String,Object> map = JSONObject.parseObject(assertDatas);
        //3-2、遍历Map
        Set<Map.Entry<String, Object>> datas = map.entrySet();
        for (Map.Entry<String, Object> keyValue : datas) {
            String key = keyValue.getKey();
            Object value = keyValue.getValue();
            //3-3、断言
            if("statuscode".equals(key)){
                //获取接口的响应状态码
                int statusCode = res.getStatusCode();
                System.out.println("断言状态码：期望值："+value+"实际值"+statusCode);
                Assert.assertEquals(statusCode,value);
            }else{
                //响应体数据断言 Gpath表达式
                Object actualValue = res.jsonPath().get(key);
                System.out.println("断言响应体：期望值："+value+"实际值"+actualValue);
                Assert.assertEquals(actualValue,value);
            }
        }
    }

    public void assertDB(String assertDB) {
        //把原始的数据库断言数据（json）转成Map
        Map<String, Object> map = JSONObject.parseObject(assertDB);
        Set<Map.Entry<String, Object>> datas = map.entrySet();
        for (Map.Entry<String, Object> keyValue : datas) {
            //map里面的key就算我们的查询sql语句
            Object actualValue = JDBCUtils.quereySingleData(keyValue.getKey());
            //map里面的value就算我们的期望值
            Assert.assertEquals(actualValue.toString(),keyValue.getValue().toString());
            //类型匹配
            //1.数据类型转账：long  int 全部toString转为字符串
        }
    }
}



















