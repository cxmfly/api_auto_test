package com.test.day01;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class RestAssuredTest1_homework {
    //get请求，请求参数跟在url后面
    @Test
    public void test11(){
        given().//请求头
        when().
                get("http://mall.lemonban.com:8107/prod/prodInfo?prodId=102").
        then().
                log().all();
    }

    //一、登录
    @Test
    public void test1(){
        String jsonData="{\"principal\":\"cxmfly\",\"credentials\":\"******\",\"appType\":3,\"loginType\":0}";
        given().//请求头
                header("Content-Type","application/json").
                body(jsonData).
        when().
                post("http://mall.lemonban.com:8107/login").
        then().
                log().all();
    }
    //二、商品搜索
    @Test
    public void test2(){
        given().//请求头
                queryParam("prodName","%E6%B4%97%E8%A1%A3%E6%9C%BA&categoryId").
                queryParam("sort",0).
                queryParam("orderBy",0).
                queryParam("current",1).
                queryParam("isAllProdType",true).
                queryParam("st",0).
                queryParam("size",12).
                when().
                get("http://mall.lemonban.com:8107/search/searchProdPage").
                then().
                log().all();
    }
    //三、选择商品
    @Test
    public void test3(){
        given().//请求头
                queryParam("prodId",102).
                when().
                get("http://mall.lemonban.com:8107/prod/prodInfo").
                then().
                log().all();
    }
    //四、添加购物车
    @Test
    public void test4(){
        given().//请求头
                header("Authorization","bearerca014c8e-86cd-4eab-9e89-0a2a02277fd3").
        when().
                get("http://mall.lemonban.com:8107/p/shopCart/prodCount").
        then().
                log().all();
    }
}
