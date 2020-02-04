package com.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by harrysa66 on 2020/2/4.
 */
public class PropertiesUtil {
    //读取配置文件（属性文件）的工具类
    private static PropertiesUtil propertiesUtil;
    private static Properties properties;
    //这里必须为private，避免外部new一个PropertiesUtil对象
    private PropertiesUtil() {
        String configFil = "message.properties";
        properties = new Properties();
        //getClassLoader()返回类加载器
        //getResourceAsStream(configFil)返回InputStream对象
        InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(configFil);
        try {
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //通过单列模式设置实例化的个数
    //对外开放的接口
    public static PropertiesUtil getInstance() {
        if (propertiesUtil == null) {
            propertiesUtil = new PropertiesUtil();
            return propertiesUtil;
        }
        return propertiesUtil;
    }

    //通过key获取对应的value值
    public String getString(String key) {
        return properties.getProperty(key);
    }
}
