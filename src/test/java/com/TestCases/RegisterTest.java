package com.TestCases;

import com.ApiDefinition.ApiCall;
import com.util.Environment;
import com.util.JDBCUtils;
import com.util.RandomDataUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegisterTest {

    @Test
    public void test_register_success(){

        //随机生成手机号码
        String randomPhone = RandomDataUtil.getUnregisterPhone();
        String randomName = RandomDataUtil.getUnregisterName();
        Environment.saveToEnvironment("randomPhone",randomPhone);
        Environment.saveToEnvironment("randomName",randomName);

        //1.发起验证码接口
        String data01="{\"mobile\":\"#randomPhone#\"}";
        ApiCall.sendRegisterSms(data01);

        //2.校验验证码接口 通过查询数据库获取验证码
        String sql="SELECT mobile_code from tz_sms_log where id = (SELECT MAX(id) FROM tz_sms_log);";
        System.out.println(sql);
        String code = (String) JDBCUtils.quereySingleData(sql);
        //将验证码校验字符串保存到环境变量中
        Environment.saveToEnvironment("code",code);

        String data02="{\"mobile\":\"#randomPhone#\",\"validCode\":\"#code#\"}";
        Response checkRes = ApiCall.checkRegisterSms(data02);
        //拿到纯文本类型的数据
        String checkSms = checkRes.body().asString();
        //将验证码校验字符串保存到环境变量中
        Environment.saveToEnvironment("checkSms",checkSms);

        //3.注册接口请求
        String data03="{\"appType\":3,\"checkRegisterSmsFlag\":\"#checkSms#\"," +
                "\"mobile\":\"#randomPhone#\",\"userName\":\"#randomName#\",\"password\":\"123456\"," +
                "\"registerOrBind\":1,\"validateType\":1}";
        Response registerRes = ApiCall.register(data03);

        //4.响应断言
        Assert.assertEquals(registerRes.getStatusCode(),200);
        Assert.assertEquals(registerRes.jsonPath().get("nickName"),randomName);

        //5.数据库断言
        String assertSql="SELECT count(*) FROM tz_user WHERE user_mobile='#randomPhone#';";
        Assert.assertEquals((long)JDBCUtils.quereySingleData(assertSql),1);
    }
}
