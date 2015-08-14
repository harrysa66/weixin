package com.weixin.util;

import java.util.Arrays;

public class CheckUtil {
	private static final String token = "harrysa66";
	public static boolean checkSignature(String signature,String timestamp,String nonce){
		String[] arr = new String[]{token,timestamp,nonce};
		//排序
		Arrays.sort(arr);
		//生成字符串
		StringBuilder content = new StringBuilder();
		for(int i = 0 ; i < arr.length ; i++){
			content.append(arr[i]);
		}
		//sha1加密
		String temp = SHA1.hex_sha1(content.toString());
		//比较加密签名
		return temp.equals(signature);
	}

}
