package com.weixin.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by harrysa66 on 2020/2/4.
 */
public class PropertiesUtil {
    /**
     * 根据key读取value
     * @param filePath
     * @param key
     * @return
     */
    public static String readValue(String filePath,String key) {
        Properties props = new Properties();
        try {
            InputStream in = Object.class.getResourceAsStream(filePath);
            props.load(in);
            String value = props.getProperty (key);
            System.out.println(key+"----"+value);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
