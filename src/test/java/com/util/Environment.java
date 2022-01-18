package com.util;

import com.alibaba.fastjson.JSONObject;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Environment {
    //设计一个Map结构 类似于postman的环境变量区域
    public static Map<String,Object> envMap=new HashMap<String, Object>();

    //向环境变量中存储键值对
    public static void saveToEnvironment(String varName,Object varValue){
        Environment.envMap.put(varName,varValue);
    }
    //从环境变量中取得对应的值
    public static Object getToEnvironment(String varName){
        return Environment.envMap.get(varName);
    }

    public static String replaceParams(String inputParam){
        //识别#XXX#表达式
        String regex="#(.+?)#";
        //编译得到Pattren模式对象
        Pattern pattern = Pattern.compile(regex);
        //通过pattern的matcher匹配，得到匹配器
        Matcher matcher = pattern.matcher(inputParam);
        //循环在原始的字符串中来找符合正则表达式的字符串
        while (matcher.find()){
            //matcher.group(0)匹配整个字符串
            String wholeStr = matcher.group(0);
            //matcher.group(0)匹配分组的第一个结果#XXX#,里面的XXX
            String subStr = matcher.group(1);
            //替换#XXX#
            inputParam=inputParam.replace(wholeStr,Environment.getToEnvironment(subStr)+"");

        }
        return inputParam;

    }

    //对map做参数化替换 方法重载
    public static Map replaceParams(Map headersMap){
        //把Map转字符串
        String datas = JSONObject.toJSONString(headersMap);
        //识别#XXX#表达式
//        String regex="#(.+?)#";
//        //编译得到Pattren模式对象
//        Pattern pattern = Pattern.compile(regex);
//        //通过pattern的matcher匹配，得到匹配器
//        Matcher matcher = pattern.matcher(datas);
//        //循环在原始的字符串中来找符合正则表达式的字符串
//        while (matcher.find()){
//            //matcher.group(0)匹配整个字符串
//            String wholeStr = matcher.group(0);
//            //matcher.group(0)匹配分组的第一个结果#XXX#,里面的XXX
//            String subStr = matcher.group(1);
//            //替换#XXX#
//            datas=datas.replace(wholeStr,Environment.getToEnvironment(subStr)+"");
//
//        }
        datas=replaceParams(datas);
        //把字符串再转成Map
        Map map = JSONObject.parseObject(datas);
        return map;

    }
    @Test
    public static void test1(){
        String inputParam="{\"basketId\":0,\"count\":1,\"prodId\":#prodId#,\"shopId\":1,\"skuId\":#skuId#}";
        //1.将对应的值保存到环境变量中
        Environment.saveToEnvironment("prodId",101);
        Environment.saveToEnvironment("skuId",203);
        //2.replaceParams方法完成替换
        System.out.println(replaceParams(inputParam));
    }
}
