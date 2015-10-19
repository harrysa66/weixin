package com.product.express.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.product.express.po.Express;
import com.product.express.po.ExpressData;
import com.weixin.constant.KeyConstant;
import com.weixin.util.DateUtil;
import com.weixin.util.WeiXinUtil;

public class ExpressUtil {
	
	public static final String STATUS_NO = "-1";
	public static final String STATUS_ERROR = "0";
	public static final String STATUS_NO_EXPRESS = "1";
	
	/**
	 * 获得快递单号信息
	 * @param expName 快递中文名称
	 * @param nu 快递单号
	 * @return
	 * @throws IOException
	 */
	public static Express getExpressInfo(String expName,String nu) throws IOException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String currentDate = sdf.format(DateUtil.getCurrentDate());
		String param = "showapi_appid="+KeyConstant.SHOWAPI_APPID+"&showapi_timestamp="+currentDate+"&showapi_sign="+KeyConstant.SHOWAPI_KEY+"&";
		Express express = new Express();
		//得到快递简称
		JSONObject expressJson = WeiXinUtil.doGetStr("http://route.showapi.com/64-17?"+param+"comName="+expName);
		if(expressJson.getJSONObject("showapi_res_body").getString("flag").equals("true")){
			String com = expressJson.getJSONObject("showapi_res_body").getString("com");
			//得到快递单号信息
			JSONObject expressInfoJson = WeiXinUtil.doGetStr("http://route.showapi.com/64-19?"+param+"com="+com+"&nu="+nu);
			JSONObject tempJson = expressInfoJson.getJSONObject("showapi_res_body");
			JSONArray expressArray = new JSONArray();
			if(tempJson.get("data") != null){
				expressArray = tempJson.getJSONArray("data");
			}
			if(expressArray.size() > 0){
				express.setExpTextName(expressJson.getJSONObject("showapi_res_body").getString("comName"));
				express.setExpSpellName(tempJson.getString("expSpellName"));
				express.setMailNo(tempJson.getString("mailNo"));
				Date updateDate= new Date(Long.parseLong(tempJson.getString("update").trim()));  
			    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			    String updateStr = formatter.format(updateDate);
				express.setUpdate(updateStr);
				express.setTel(expressJson.getJSONObject("showapi_res_body").getString("phone"));
				express.setStatus(tempJson.getString("status"));
				List<ExpressData> expressDataList = new ArrayList<ExpressData>();
				ExpressData expressData = null;
				for(int i = 0 ; i < expressArray.size() ; i++){
					expressData = new ExpressData();
					expressData.setTime(expressArray.getJSONObject(i).getString("time"));
					expressData.setContext(expressArray.getJSONObject(i).getString("context"));
					expressDataList.add(expressData);
				}
				express.setExpressDataList(expressDataList);
			}else{
				String msg = "";
				if(tempJson.get("message") != null){
					msg = tempJson.getString("message");
				}else if(tempJson.get("msg") != null){
					msg = tempJson.getString("msg");
				}
				express.setStatus(tempJson.getString("status"));
				express.setMsg(msg);
			}
		}else{//快递不存在
			express.setStatus("-2");
			express.setMsg("查询失败");
		}
		return express;
	}

}
