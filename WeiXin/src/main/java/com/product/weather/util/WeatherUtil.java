package com.product.weather.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.product.weather.po.Index;
import com.product.weather.po.Weather;
import com.product.weather.po.WeatherData;
import com.weixin.constant.KeyConstant;
import com.weixin.util.WeiXinUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WeatherUtil {
	
	public static final String ERROR_OK = "0";
	public static final String ERROR_NO_RESULT = "-3";
	public static final String STATUS_SUCCESS = "success";
	
	/**
	 * 获得天气信息
	 * @param currentCity 城市名称
	 * @return
	 * @throws IOException
	 */
    public static Weather getWeather(String currentCity) throws IOException{
    	JSONObject jsonData = WeiXinUtil.doGetStr("http://api.map.baidu.com/telematics/v3/weather?output=json&location="+currentCity+"&ak="+KeyConstant.BAIDU_AK);
        Weather weather = new Weather();
        String error = jsonData.getString("error");
        String status = jsonData.getString("status");
        if(error.equals(ERROR_OK) && status.toLowerCase().equals(STATUS_SUCCESS)){
        	weather.setStatus(error);
        	weather.setDate(jsonData.getString("date"));//当前时间
            JSONArray results = jsonData.getJSONArray("results");
            JSONObject weatherInfo = results.getJSONObject(0);
            weather.setCurrentCity(weatherInfo.getString("currentCity"));//当前城市
            //PM2.5
            String pm25 = weatherInfo.getString("pm25");
            if(pm25 != null && !"".equals(pm25)){
            	int pm25Int = Integer.parseInt(pm25);
                if(pm25Int>=0 && pm25Int<=50){
                	weather.setPm25(pm25+"优");
                }else if(pm25Int>=51 && pm25Int<=100){
                	weather.setPm25(pm25+"良");
                }else if(pm25Int>=101 && pm25Int<=150){
                	weather.setPm25(pm25+"轻度污染");
                }else if(pm25Int>=151 && pm25Int<=200){
                	weather.setPm25(pm25+"中度污染");
                }else if(pm25Int>=201 && pm25Int<=300){
                	weather.setPm25(pm25+"重度污染");
                }else if(pm25Int>300){
                	weather.setPm25(pm25+"严重污染");
                }else{
                	weather.setPm25(pm25);
                }
            }else{
            	weather.setPm25(pm25);
            }
            
            //指数信息
            JSONArray indexArray = weatherInfo.getJSONArray("index");
            List<Index> indexList = new ArrayList<Index>();
            Index index = null;
            for(int i = 0 ; i < indexArray.size() ; i++){
            	index = new Index();
            	String title = indexArray.getJSONObject(i).getString("title");
            	String zs = indexArray.getJSONObject(i).getString("zs");
            	String tipt = indexArray.getJSONObject(i).getString("tipt");
            	String des = indexArray.getJSONObject(i).getString("des");
            	index.setTitle(title);
            	index.setZs(zs);
            	index.setTipt(tipt);
            	index.setDes(des);
            	indexList.add(index);
            }
            weather.setIndexList(indexList);
            //天气信息
            JSONArray weatherDataArray = weatherInfo.getJSONArray("weather_data");
            List<WeatherData> weatherDataList = new ArrayList<WeatherData>();
            WeatherData weatherData = null;
            for(int j = 0 ; j < weatherDataArray.size() ; j++){
            	weatherData = new WeatherData();
            	String date = weatherDataArray.getJSONObject(j).getString("date");
            	String dayPictureUrl = weatherDataArray.getJSONObject(j).getString("dayPictureUrl");
            	String nightPictureUrl = weatherDataArray.getJSONObject(j).getString("nightPictureUrl");
            	String weatherDesc = weatherDataArray.getJSONObject(j).getString("weather");
            	String wind = weatherDataArray.getJSONObject(j).getString("wind");
            	String temperature = weatherDataArray.getJSONObject(j).getString("temperature");
            	weatherData.setDate(date);
            	weatherData.setDayPictureUrl(dayPictureUrl);
            	weatherData.setNightPictureUrl(nightPictureUrl);
            	weatherData.setWeather(weatherDesc);
            	weatherData.setWind(wind);
            	weatherData.setTemperature(temperature);
            	weatherDataList.add(weatherData);
            }
            weather.setWeatherDataList(weatherDataList);
        }else{
        	weather.setStatus(error);
        }
    	return weather;
    }
    
    public static void main(String[] args) throws IOException {
    	getWeather("天津");
	}

}
