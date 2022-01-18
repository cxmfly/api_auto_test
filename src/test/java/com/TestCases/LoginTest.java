package com.TestCases;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.ApiDefinition.ApiCall;
import com.alibaba.fastjson.JSONObject;
import com.common.BaseTest;
import com.pojo.CaseData;
import com.util.ExcelUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoginTest extends BaseTest {
    @Test
    public void login_test(){
       //1.准备测试数据
       String jsonData="{\"principal\":\"cxmfly\",\"credentials\":\"cxmfly54621323\",\"appType\":3,\"loginType\":0}";
       //2.直接调用登录的接口请求
       Response res = ApiCall.login(jsonData);
       //3.断言
       int statusCode = res.getStatusCode();
       Assert.assertEquals(statusCode,200);
       Object nickName = res.jsonPath().get("nickName");
       Assert.assertEquals(nickName,"cxmfly");
   }

    @DataProvider
    public Object[] getLoginDatas(){
        //一维数组/二维数组
        Object[] data = {"{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}",
                "{\"principal\":\"\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}",
                "{\"principal\":\"waiwai\",\"credentials\":\"\",\"appType\":3,\"loginType\":0}",
                "{\"principal\":\"lemon1111\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}"};
        return data;
    }

    @Test(dataProvider = "getLoginDatas" )
    public void test_login_from_array(String caseData){
        //1、准备测试数据
        //String jsonData="{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";
        //2、直接调用登录的接口请求
        Response res = ApiCall.login(caseData);
        //3、断言
        /*int statusCode = res.getStatusCode();
        Assert.assertEquals(statusCode,2000);
        String nickName = res.jsonPath().get("nickName");
        Assert.assertEquals(nickName,"waiwai");*/
    }


    @Test(dataProvider = "getLoginDatasFromExcel" )
    public void test_login_from_excel(CaseData caseData){
        //1、准备测试数据
        //String jsonData="{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";
        //2、直接调用登录的接口请求
        Response res = ApiCall.login(caseData.getInputParams());
        //3、断言
        String assertDatas = caseData.getAssertResponse();
        //statuscode:200,nickName:"waiwai"
        //3-1、json字符串怎么转成Java里面的Map
        assertResponse(assertDatas,res);

    }


    @DataProvider
    public Object[] getLoginDatasFromExcel(){
//        //1、读取Excel？？Easypoi
//        ImportParams importParams = new ImportParams();
//        importParams.setStartSheetIndex(0);
//        //读取的文件src\test\resources\caseData.xlsx
//        List<CaseData> datas = ExcelImportUtil.importExcel(new File("src\\test\\resources\\caseData.xlsx"),
//                CaseData.class,importParams);
       //集合转成一维数组
//        return datas.toArray();
        return   ExcelUtil.readExcel(0).toArray();
    }


}
