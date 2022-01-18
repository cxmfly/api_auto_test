package com.test.day01;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class RestAssuredTest2_homework {
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
                queryParam("prodName","洗衣机").
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
//    @Test
//    public void test4(){
//        String jsonData="{\"basketId\":0,\"count\":1,\"prodId\":\"102\",\"shopId\":1,\"skuId\":436}";
//        given().//请求头
//                header("Authorization","bearer889adfae-cf44-4eb2-a142-8e03c25af561").
//                header("Content-Type","application/json").
//                body(jsonData).
//        when().
//                post("http://mall.lemonban.com:8107/p/shopCart/changeItem").
//        then().
//                log().all();
//    }
}
