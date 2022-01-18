package com.test.day01;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class RestAssuredTest2 {
    //get请求，请求参数跟在url后面
    @Test
    public void test11(){
        Response res=
        given().//请求头
        when().
                get("http://mall.lemonban.com:8107/prod/prodInfo?prodId=102").
        then().
                log().all().
                extract().response();

        //响应状态码
        /*System.out.println(res.getStatusCode());
        //接口的响应时间
        System.out.println(res.time());
        //获取接口所有的响应头信息
        System.out.println(res.getHeaders());
        //获取某个指定的响应头
        System.out.println(res.getHeader("Content-Type"));
*/

//        System.out.println(res.jsonPath().get("skuList[0].skuId"));
    }
    @Test
    public void test1(){
        String jsonData="{\"principal\":\"cxmfly\",\"credentials\":\"******\",\"appType\":3,\"loginType\":0}";
        Response res=
        given().//请求头
                header("Content-Type","application/json").
                body(jsonData).
                when().
                post("http://mall.lemonban.com:8107/login ").
                then().
                log().all().
                extract().response();

        //解析响应体数据
//        System.out.println(res.jsonPath().get("nickName"));

    }

    @Test
    public void test111(){
        String jsonData="{\"principal\":\"cxmfly\",\"credentials\":\"******\",\"appType\":3,\"loginType\":0}";
        Response res=
                given().//请求头
                        header("Content-Type","application/json").
                        body(jsonData).
                        when().
                        post("https://www.baidu.com").
                        then().
                        log().all().
                        extract().response();

        //解析响应体数据
//        System.out.println(res.jsonPath().get("nickName"));
    }

}
