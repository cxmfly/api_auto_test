package com.TestCases;

import com.common.BaseTest;
import com.ApiDefinition.ApiCall;
import com.common.BaseTest;
import com.pojo.CaseData;
import com.service.BusinessFlow;
import com.util.Environment;
import com.util.ExcelUtil;
import com.util.JDBCUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * @author 歪歪欧巴
 * @Description TODO
 * @date 2021/12/27 21:37
 * @Copyright 湖南省零檬信息技术有限公司. All rights reserved.
 */
public class ShopcartTest extends BaseTest {

    @Test
    public void test_add_shopcart_success(){
        //添加购物车用例
        //1、准备测试数据
        //2、发起接口请求
        //2-1、登录
        /*String loginData="{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";
        Response loginRes = ApiCall.login(loginData);
        //提取token
        String token = loginRes.jsonPath().get("access_token");
        //2-2、搜索商品
        String searchData = "prodName=&categoryId=&sort=0&orderBy=0&current=1&isAllProdType=true&st=0&size=12";
        Response searchRes = ApiCall.searchProduct(searchData);
        //提取商品ID
        int prodId = searchRes.jsonPath().get("records[0].prodId");
        //2-3、商品信息
        Response infoRes = ApiCall.productInfo(prodId);*/
        Response infoRes = BusinessFlow.login_search_info();
        //提取skuId
        int skuId = infoRes.jsonPath().get("skuList[0].skuId");
        Environment.saveToEnvironment("skuId",skuId);
        //2-4、添加到购物车
        String shopCartData="{\"basketId\":0,\"count\":1,\"prodId\":\"#prodId#\",\"shopId\":1,\"skuId\":#skuId#}";
        Response shopCartRes = ApiCall.add1Cart(shopCartData,(String)Environment.getToEnvironment("token"));
        //3、响应断言
        Assert.assertEquals(shopCartRes.getStatusCode(),200);
        //4.数据库断言
        String assertSql="SELECT count(*) FROM tz_basket WHERE user_id  =(SELECT user_id FROM tz_user WHERE user_name='Larson')";
        //根据购物车商品的数量断言
        Assert.assertEquals((long)JDBCUtils.quereySingleData(assertSql),1);

    }

    @Test(dataProvider = "getShopCartDatas")
    public void test_shortcart(CaseData caseData){
        //测试数据
        //登录获取token
        Response loginRes = ApiCall.login("{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}");
        String token = loginRes.jsonPath().get("access_token");
        String inputParams = caseData.getInputParams();
        Response addShopCartRes = ApiCall.add1Cart(inputParams, token);
        //断言
        assertResponse(caseData.getAssertResponse(),addShopCartRes);
        //数据库断言
        assertDB(caseData.getAssertDB());

    }

    @DataProvider
    public Object[] getShopCartDatas(){
        Object[] caseDatas = ExcelUtil.readExcel(1).toArray();
        return caseDatas;
    }
}






