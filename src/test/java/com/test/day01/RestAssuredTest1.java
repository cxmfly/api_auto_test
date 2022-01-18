package com.test.day01;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

public class RestAssuredTest1 {
    //get请求，请求参数跟在url后面
    @Test
    public void test11(){
        given().//请求头
        when().
                get("http://mall.lemonban.com:8107/prod/prodInfo?prodId=102").
        then().
                log().all();
    }
    //get请求，请求参数写在given（）后面
    @Test
    public void test12(){
        given().//请求头
                queryParam("prodId",102).
        when().
                get("http://mall.lemonban.com:8107/prod/prodInfo").
        then().
                log().all();
    }
    //post请求 josn传参
    @Test
    public void test21(){
        String jsonData="{\"principal\":\"cxmfly\",\"credentials\":\"******\",\"appType\":3,\"loginType\":0}";
        given().//请求头
                header("Content-Type","application/json").
                body(jsonData).
        when().
                post("http://mall.lemonban.com:8107/login ").
        then().
                log().all();
    }

}
