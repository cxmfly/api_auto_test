package com.TestCases;

import com.ApiDefinition.ApiCall;
import com.service.BusinessFlow;
import com.util.Environment;
import com.util.JDBCUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlaceOrderTest {
    @Test
    public void test_place_order_success(){
        Response prodInfoRes = BusinessFlow.login_search_info();
        //获取库存ID
        Environment.saveToEnvironment("skuId",prodInfoRes.jsonPath().get("skuList[0].skuId"));

        //1.确认订单
        String confirmDatas="{\"addrId\":0,\"orderItem\":{\"prodId\":#prodId#,\"skuId\":#skuId#,\"prodCount\":1,\"shopId\":1}" +
                ",\"couponIds\":[],\"isScorePay\":0,\"userChangeCoupon\":0,\"userUseScore\":0," +
                "\"uuid\":\"6e2f3a7b-1bdd-4252-81d5-9985c8fb1ff7\"}";
        Response confirmRes = ApiCall.confirmOrder(confirmDatas,"#token#" );

        //2.提交订单
        String submitData = "{\"orderShopParam\":[{\"remarks\":\"\",\"shopId\":1}]," +
                "\"uuid\":\"6e2f3a7b-1bdd-4252-81d5-9985c8fb1ff7\"}";
        Response submitRes = ApiCall.submitOrder(submitData,"#token#" );
        //获取订单号
        String orderNumbers = submitRes.jsonPath().get("orderNumbers");
        Environment.saveToEnvironment("orderNumbers",orderNumbers);

        //3.下单支付
        String payOrderData = "{\"payType\":3,\"orderNumbers\":\"#orderNumbers#\"}";
        ApiCall.payOrder(payOrderData,"#token#");

        //4.模拟回调--模拟真实的支付流程
        String mockPayData = "{\n" +
                "    \"payNo\":#orderNumbers#, \n" +
                "    \"bizPayNo\":\"XXXX\",\n" +
                "    \"isPaySuccess\":true\n" +
                "}";
        Response mockPayRes = ApiCall.mockPay(mockPayData, "#token#");
        //响应断言
        //提取纯文本响应体数据
        String actual = mockPayRes.body().asString();
        Assert.assertEquals(actual,"success");
        //数据库断言
        String sql = "SELECT STATUS FROM tz_order WHERE order_number='#orderNumbers#'";
        Object actualDB = JDBCUtils.quereySingleData(sql);
        Assert.assertEquals(actualDB,2);
    }
}












