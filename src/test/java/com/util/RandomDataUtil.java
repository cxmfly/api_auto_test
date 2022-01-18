package com.util;

import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.Locale;

public class RandomDataUtil {

    public static String getUnregisterPhone(){
        //随机生成手机号码
        Faker faker = new Faker(Locale.CHINA);
        String randomPhone = faker.phoneNumber().cellPhone();
        System.out.println(randomPhone);
        //查库，去判别数据库中有没有这个手机号码
        String sql="SELECT count(*) FROM tz_user WHERE user_mobile='"+randomPhone+"';";
        //3.循环遍历
        while (true){
            Long count = (Long) JDBCUtils.quereySingleData(sql);
            if(count==0){
                //没有被注册--符合要求
                break;
            }else if(count==1){
                //已经注册--再次生成一个新的手机号码
                randomPhone = faker.phoneNumber().cellPhone();
                sql="SELECT count(*) FROM tz_user WHERE user_mobile='"+randomPhone+"';";
            }
        }
        return randomPhone;

    }

    public static String getUnregisterName(){
        //1.随机生成用户名
        Faker faker = new Faker();
        String randomName= faker.name().lastName();
        //查库，去判别数据库中有没有这个手机号码
        String sql="SELECT count(*) FROM tz_user WHERE user_name='"+randomName+"';";
        //3.循环遍历
        while (true){
            Long count = (Long) JDBCUtils.quereySingleData(sql);
            if(count==0){
                //没有被注册--符合要求
                break;
            }else if(count==1){
                //已经注册--再次生成一个新的手机号码
                randomName= faker.name().lastName();
                sql="SELECT count(*) FROM tz_user WHERE user_name='"+randomName+"';";
            }
        }
        return randomName;

    }

}
