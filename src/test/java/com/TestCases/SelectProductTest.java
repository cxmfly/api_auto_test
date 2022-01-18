package com.TestCases;

import com.ApiDefinition.ApiCall;
import com.common.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SelectProductTest extends BaseTest {
    @Test
    public void selectProduct_test(){
        //1.准备测试数据
        int prodId=102;
        //2.发起接口请求
        Response res = ApiCall.selectProduct(prodId);
        //3.断言
        //3-1、响应状态码
        int status = res.getStatusCode();
        Assert.assertEquals(status,200);
        //3-2、根据prodName是否包含“冰箱”
        String prodName=res.jsonPath().get("prodName");
        Assert.assertTrue(prodName.contains("小天鹅"));

    }
}
