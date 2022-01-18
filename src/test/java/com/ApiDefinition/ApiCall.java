package com.ApiDefinition;

import com.common.GlobalConfig;
import com.util.Environment;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.testng.annotations.Test;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class ApiCall {
    public static Response request(String method, String url, Map headersMap,String inputParams){
        String logFilePath=null;
        if(!GlobalConfig.IS_DEBUG) {
            //把所有的接口日志（请求+响应）重定向到本地指定文件中
//        PrintStream fileOutPutStream = null;
//        try {
//            fileOutPutStream = new PrintStream(new File("log/test_all.log"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

            //每个接口请求的日志单独的保存到本地的每一个文件中
            PrintStream fileOutPutStream = null;
            //设置日志文件的地址
            String logFileDir="target/log/";
            File file = new File(logFileDir);
            if(!file.exists()){
                file.mkdirs();
            }
             logFilePath = logFileDir+"test_"+System.currentTimeMillis()+".log";
            try {
                fileOutPutStream = new PrintStream(new File(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutPutStream));

            RestAssured.filters(new RequestLoggingFilter(fileOutPutStream),new ResponseLoggingFilter(fileOutPutStream));
        }


        //添加日志信息到Allure报表中
        if(!GlobalConfig.IS_DEBUG) {
            try {
                Allure.addAttachment("接口的请求/响应信息",new FileInputStream(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        //参数化替换
        //1.接口入参做参数化替换
        inputParams = Environment.replaceParams(inputParams);
        //2.接口请求头参数化替换
        headersMap=Environment.replaceParams(headersMap);
        //3.接口的url地址参数化替换
        url=Environment.replaceParams(url);
        //指定项目base url  http://mall.lemonban.com:8107
        RestAssured.baseURI=GlobalConfig.url;
        Response res = null;
        if("get".equalsIgnoreCase(method)){
            res=
                given().//请求头
                        headers(headersMap).log().all().
                when().
                        get(url+"?"+inputParams).
                then().
                        log().all().
                        extract().response();
        }else if("post".equalsIgnoreCase(method)){
            res=
                given().//请求头
                        headers(headersMap).
                        body(inputParams).log().all().
                when().
                        post(url).
                then().
                        log().all().
                        extract().response();
        }else if("put".equalsIgnoreCase(method)){
           //TO DO
            res=
                    given().//请求头
                            headers(headersMap).
                            body(inputParams).log().all().
                            when().
                            put(url).
                            then().
                            log().all().
                            extract().response();
        }else if("delete".equalsIgnoreCase(method)){
            res=
                    given().//请求头
                            headers(headersMap).
                            body(inputParams).log().all().
                            when().
                            delete(url).
                            then().
                            log().all().
                            extract().response();
        }

        return res;
    }
    //一、登录
    public static Response login(String inputParams){
        HashMap<Object, Object> headmap = new HashMap<>();
        headmap.put("Content-Type","application/json");
        return request("post","/login",headmap,inputParams);
//        Response res=
//        given().//请求头
//                headers("Content-Type","application/json").
//                body(inputParams).log().all().
//        when().
//                post("http://mall.lemonban.com:8107/login").
//        then().
//                log().all().
//                extract().response();
//        return res;
    }
    //二、商品搜索
    public static Response searchProduct(String inputParams){
//        inputParams = Environment.replaceParams(inputParams);
        HashMap<Object, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        return request("get","/search/searchProdPage",headMap,inputParams);
//        Response res=
//        given().//请求头
//        when().
//                get("http://mall.lemonban.com:8107/search/searchProdPage?"+inputParams).
//        then().
//                log().all().
//                extract().response();
//        return res;
    }
    //三、选择商品
    public static Response selectProduct(int prodId){
        HashMap<Object, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        return request("get","/prod/prodInfo",headMap,"prodId="+prodId);
//        Response res=
//        given().//请求头
//        when().
//                get("http://mall.lemonban.com:8107/prod/prodInfo?prodId="+prodId).
//        then().
//                log().all().
//                extract().response();
//        return res;

    }
    //四、添加购物车
    public static Response add1Cart(String inputParams,String token){
        //对接口的入参进行替换
//        inputParams = Environment.replaceParams(inputParams);
        HashMap<Object, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","bearer"+token);
        return request("post","/p/shopCart/changeItem",headMap,inputParams);


//        Response res=
//        given().//请求头
//                header("Authorization","bearer"+token).
//                header("Content-Type","application/json").
//                body(inputParams).log().all().
//                post("http://mall.lemonban.com:8107/p/shopCart/changeItem").
//        then().
//                log().all().
//                extract().response();
//        return res;

    }
    //注册验证码发送接口
//    请求参数：{"mobile":"13642308713"}
//    响应数据：无
    public static Response sendRegisterSms(String inputParams){
        HashMap<Object, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        return request("put","/user/sendRegisterSms",headMap,inputParams);

    }
    //校验验证码发送接口
//    请求参数：{"mobile":"13642308713","validCode":"907814"}
//    响应数据：无
    public static Response checkRegisterSms(String inputParams){
        HashMap<Object, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        return request("put","/user/checkRegisterSms",headMap,inputParams);

    }

    //注册接口
//    请求参数：{"appType":3,"checkRegisterSmsFlag":"d8d5ef157b594f1ea0c56e411f4cd7c1",
//    "mobile":"13642308713","userName":"cxmfly2","password":"123456","registerOrBind":1,"validateType":1}
//    响应数据：无
    public static Response register(String inputParams){
        HashMap<Object, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        return request("put","/user/registerOrBindUser",headMap,inputParams);

    }

    //确认订单接口定义
    public static Response confirmOrder(String inputParams,String token){
        HashMap<Object, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","bearer"+token);
        return request("post","/p/order/confirm",headMap,inputParams);
    }

    //提交订单接口定义
    public static Response submitOrder(String inputParams,String token){
        HashMap<Object, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","bearer"+token);
        return request("post","/p/order/submit",headMap,inputParams);
    }

    //支付下单接口定义
    public static Response payOrder(String inputParams,String token){
        HashMap<Object, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","bearer"+token);
        return request("post","/p/order/pay",headMap,inputParams);
    }

    //模拟支付回调的接口
    public static Response mockPay(String inputParams,String token){
        HashMap<Object, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","bearer"+token);
        return request("post","/notice/pay/3",headMap,inputParams);
    }
    //erp项目
    public static Response erpLogin(String inputParams){
        HashMap<Object, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/x-www-form-urlencoded");
        return request("post","/user/login",headMap,inputParams);
    }
    //前程贷登录接口
    public static Response futureloanLogin(String inputParams){
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("X-Lemonban-Media-Type","lemonban.v3");
        return request("post","/futureloan/member/login",headMap,inputParams);
    }
    //前程贷充值接口
    public static Response futureloanRecharge(String inputParams,String token){
        HashMap<Object, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("X-Lemonban-Media-Type","lemonban.v3");
        headMap.put("Authorization","Bearer "+token);
        return request("post","/futureloan/member/recharge",headMap,inputParams);
    }




}












