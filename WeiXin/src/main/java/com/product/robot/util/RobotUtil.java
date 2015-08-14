package com.product.robot.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.product.robot.po.CookBook;
import com.product.robot.po.Flight;
import com.product.robot.po.News;
import com.product.robot.po.Robot;
import com.product.robot.po.Train;
import com.weixin.constant.KeyConstant;
import com.weixin.util.WeiXinUtil;

public class RobotUtil {
	
	public static final String TEXT_CODE = "100000";
	public static final String URL_CODE = "200000";
	public static final String NEWS_CODE = "302000";
	public static final String TRAIN_CODE = "305000";
	public static final String FLIGHT_CODE = "306000";
	public static final String COOK_CODE = "308000";
	public static final String EMPTY_CODE = "40002";
	
	public static final String DOMAIN = "http://harrysa66.tunnel.mobi";
	//public static final String DOMAIN = "http://www.hebingqing.cn";
	public static final String IMAGE_LOC = "/WeiXin/weixin_image";
	
	public static final String NEWS_IMAGE = DOMAIN+IMAGE_LOC+"/weixin-xinwen.png";
	public static final String TRAIN_IMAGE = DOMAIN+IMAGE_LOC+"/weixin-lieche.png";
	public static final String FLIGHT_IMAGE = DOMAIN+IMAGE_LOC+"/weixin-hangban.png";
	public static final String COOK_IMAGE = DOMAIN+IMAGE_LOC+"/weixin-caipu.png";
	
	public static <T> Robot<T> getRobotInfo(String userid,String info) throws IOException{
		Robot<T> robot = null;
		String infoEncode = URLEncoder.encode(info, "utf-8");
		JSONObject jsonData = WeiXinUtil.doGetStr("http://www.tuling123.com/openapi/api?userid="+userid+"&key="+KeyConstant.ROBOT_KEY+"&info="+infoEncode);
		String code = jsonData.getString("code");
		Map<String, Class> map = new HashMap<String, Class>();
		if(code.equals(NEWS_CODE)){
			map.put("list", News.class);
		}
		if(code.equals(TRAIN_CODE)){
			map.put("list", Train.class);
		}
		if(code.equals(FLIGHT_CODE)){
			map.put("list", Flight.class);
		}
		if(code.equals(COOK_CODE)){
			map.put("list", CookBook.class);
		}
		robot =(Robot<T>) JSONObject.toBean(jsonData, Robot.class,map);
		return robot;
	}
	
	public static void main(String[] args) {
		try {
			Robot<Object> robot = getRobotInfo("eb2edb736","350342680333");
			//CookBook[] cookBook = (CookBook[]) robot.getList();
			System.out.println(robot.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
