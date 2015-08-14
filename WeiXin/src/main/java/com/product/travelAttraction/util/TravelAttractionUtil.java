package com.product.travelAttraction.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.product.travelAttraction.po.Attention;
import com.product.travelAttraction.po.TravelAttraction;
import com.weixin.constant.KeyConstant;
import com.weixin.util.PinyinUtil;
import com.weixin.util.WeiXinUtil;

public class TravelAttractionUtil {
	
	public static final String ERROR_OK = "0";
	public static final String ERROR_NO_RESULT = "-3";
	public static final String STATUS_SUCCESS = "success";
	public static final String LOCATION_PIC = "http://api.map.baidu.com/staticimage?width=280&height=140&zoom=15&scale=2&";
	
	public static TravelAttraction getTravelAttractionInfo(String taName) throws IOException{
		String taPyName = PinyinUtil.converterToSpell(taName);
		JSONObject jsonData = WeiXinUtil.doGetStr("http://api.map.baidu.com/telematics/v3/travel_attractions?output=json&id="+taPyName+"&ak="+KeyConstant.BAIDU_AK);
		TravelAttraction travelAttraction = new TravelAttraction();
		String error = jsonData.getString("error");
        String status = jsonData.getString("status");
        JSONObject result = jsonData.getJSONObject("result");
		
        if(error.equals(ERROR_OK) && status.toLowerCase().equals(STATUS_SUCCESS)){
        	travelAttraction.setStatus(error);
        	travelAttraction.setName(result.getString("name"));
        	travelAttraction.setUrl(result.getString("url"));
        	travelAttraction.setLng(result.getJSONObject("location").getString("lng"));
        	travelAttraction.setLat(result.getJSONObject("location").getString("lat"));
        	travelAttraction.setTelephone(result.getString("telephone"));
        	travelAttraction.setAbstracts(result.getString("abstract"));
        	travelAttraction.setDescription(result.getString("description"));
        	if(null != result.get("ticket_info")){
        		JSONObject ticketInfo = result.getJSONObject("ticket_info");
            	travelAttraction.setPrice(ticketInfo.getString("price"));
            	travelAttraction.setOpenTime(ticketInfo.getString("open_time"));
            	if(null != ticketInfo.get("attention")){
            		JSONArray attentionArray = ticketInfo.getJSONArray("attention");
                	List<Attention> attentionList = new ArrayList<Attention>();
                	Attention attention = null;
                	for(int i = 0 ; i < attentionArray.size() ; i++){
                		attention = new Attention();
                		attention.setName(attentionArray.getJSONObject(i).getString("name"));
                		attention.setDescription(attentionArray.getJSONObject(i).getString("description"));
                		attentionList.add(attention);
                	}
                	travelAttraction.setAttentionList(attentionList);
            	}
        	}
        	travelAttraction.setStatus(error);
        	
        }else{
        	travelAttraction.setStatus(error);
        }
        
		return travelAttraction;
	}

}
