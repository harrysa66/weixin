package com.product.traffic.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import com.product.traffic.po.Traffic;
import com.product.traffic.po.TrafficEvent;
import com.weixin.constant.KeyConstant;
import com.weixin.util.WeiXinUtil;

public class TrafficUtil {
	
	public static final String EVENT_TYPE_0 = "0";//管制
	public static final String EVENT_TYPE_1 = "1";//事故
	public static final String EVENT_TYPE_2 = "2";//施工
	public static final String EVENT_TYPE_3 = "3";//管制
	public static final String STATUS_SUCCESS = "success";//成功
	public static final String STATUS_0 = "0";//成功
	
	public static String getTrafficEvent(String location) throws IOException, ParseException{
		String url = "http://api.map.baidu.com/telematics/v3/trafficEvent?location=LOCATION&output=json&ak="+KeyConstant.BAIDU_AK;
		url = url.replace("LOCATION", URLEncoder.encode(location, "UTF-8"));
		JSONObject jsonObject = WeiXinUtil.doGetStr(url);
		StringBuilder message = new StringBuilder();
		Traffic traffic = (Traffic) JSONObject.toBean(jsonObject, Traffic.class);
		if(traffic.getError().equals(STATUS_0)){
			//message.append("当前城市：").append(traffic.getCurrentCity()).append("\n");
			//message.append("当前时间：").append(formatDate(traffic.getDateTime())).append("\n\n");
			if(traffic.getResults().length > 0){
				for(TrafficEvent event : traffic.getResults()){
					//TrafficEvent event = traffic.getResults()[i];
					/*String type = "";
					if(event.getType().equals(EVENT_TYPE_0)){
						type = "管制";
					}
					if(event.getType().equals(EVENT_TYPE_1)){
						type = "事故";
					}
					if(event.getType().equals(EVENT_TYPE_2)){
						type = "施工";
					}
					if(event.getType().equals(EVENT_TYPE_3)){
						type = "管制";
					}*/
					//message.append(event.getTitle()).append("-").append(type).append("\n");
					//message.append(formatDate(event.getStartTime())).append("-");
					//message.append(formatDate(event.getEndTime())).append("\n");
					message.append(event.getDescription()).append("\n");
					if(message.toString().getBytes("utf-8").length > 1951){//2014-63=1951
						message.substring(0, message.length()-event.getDescription().length());
						break;
					}
				}
			}
		}else{
			message.append("查询错误：").append(traffic.getStatus());
		}
		return message.toString();
	}
	
	public static String formatDate(String dateStr) throws ParseException {
		Date date = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss").parse(dateStr); 
		String dateTime = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss").format(date);
		return dateTime;
	}

}
