package com.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    //读取配置文件（属性文件）的工具类
    private static ConfigManager configManager;
    private static Properties properties;
    //这里必须为private，避免外部new一个ConfigManager对象
    private ConfigManager() {
        String configFil = "jdbc.properties";
        properties = new Properties();
        //getClassLoader()返回类加载器
        //getResourceAsStream(configFil)返回InputStream对象
        InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(configFil);
        try {
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //通过单列模式设置实例化的个数
    //对外开放的接口
    public static ConfigManager getInstance() {
        if (configManager == null) {
            configManager = new ConfigManager();
            return configManager;
        }
        return configManager;
    }

    //通过key获取对应的value值
    public String getString(String key) {
        return properties.getProperty(key);
    }

    public static void main(String[] args) {
        System.out.println(ConfigManager.getInstance().getString("driver"));
    }
}
