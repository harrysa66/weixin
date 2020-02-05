package com.weixin.util;

import java.util.Random;

/**
 * Created by harrysa66 on 2020/2/5.
 */
public class RandomUtil {

    /**
     * 生成n位随机数
     * @param digit 位数
     * @return
     */
    public static String getRandom(int digit){
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i=0 ; i < digit ; i++){
            result.append(random.nextInt(10));
        }
        return result.toString();
    }

}
