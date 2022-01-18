package com.TestCases;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class MockTest {
    @Test
    public void test_moco(){
        String inputParams = "{\n" +
                "        \"phone\": \"133131111\",\n" +
                "        \"pwd\": \"123456\"\n" +
                "      }";
        Map<String,Object> map = new HashMap<>();
        map.put("Content-Type","application/json");
        map.put("X-Lemonban-Media-Type","lemonban.v1");
        Response res=
        given().//请求头
                log().all().
                headers(map).
                body(inputParams).
        when().
                post("http://localhost:9991/login").
        then().
                log().all().
                extract().response();
    }
}
