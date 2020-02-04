package com.weixin.util;

import com.product.express.po.Express;
import com.product.express.po.ExpressData;
import com.product.express.util.ExpressUtil;
import com.product.guessNumber.util.GuessNumberUtil;
import com.product.robot.po.CookBook;
import com.product.robot.po.Flight;
import com.product.robot.po.Robot;
import com.product.robot.po.Train;
import com.product.robot.util.RobotUtil;
import com.product.surroundingSearch.util.SurroundingSearchUtil;
import com.product.traffic.util.TrafficUtil;
import com.product.trans.util.TransUtil;
import com.product.travelAttraction.po.Attention;
import com.product.travelAttraction.po.TravelAttraction;
import com.product.travelAttraction.util.TravelAttractionUtil;
import com.product.weather.po.Index;
import com.product.weather.po.Weather;
import com.product.weather.util.WeatherUtil;
import com.thoughtworks.xstream.XStream;
import com.weixin.po.message.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

public class MessageUtil {
	
	public static final String MESSAGE_TEXT = "text";//文本消息
	public static final String MESSAGE_IMAGE = "image";//图片消息
	public static final String MESSAGE_NEWS = "news";//图文消息
	public static final String MESSAGE_VOICE = "voice";//语音消息
	public static final String MESSAGE_MUSIC = "music";
	public static final String MESSAGE_VIDEO = "video";//视频消息
	public static final String MESSAGE_SHORTVIDEO = "shortvideo";//小视频消息
	public static final String MESSAGE_LOCATION = "location";//地理位置消息
	public static final String MESSAGE_LINK = "link";//链接消息
	public static final String MESSAGE_EVENT = "event";//事件推送
	public static final String MESSAGE_EVENT_SUBSCRIBE = "subscribe";//关注
	public static final String MESSAGE_EVENT_UNSUBSCRIBE = "unsubscribe";//取消关注
	public static final String MESSAGE_EVENT_SCAN = "SCAN";//用户已关注时的事件推送
	public static final String MESSAGE_EVENT_LOCATION = "LOCATION";//上报地理位置事件
	public static final String MESSAGE_EVENT_CLICK = "CLICK";//点击菜单拉取消息时的事件推送
	public static final String MESSAGE_EVENT_VIEW = "VIEW";//点击菜单跳转链接时的事件推送 
	
	/*public static final String WEATHER_KEY = "微信天气";
	public static final String EXPRESS_KEY = "微信快递";
	public static final String NEWS_WEATHER_KEY = "微信图文天气";
	public static final String TRAVEL_ATTRACTION_KEY = "微信景点";
	public static final String TRANS_KEY = "微信翻译";
	public static final String ROBOT_KEY = "聊天";*/
	
	/**
	 * xml转为map集合
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		//从request中获取输入流
		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);
		
		Element root = doc.getRootElement();
		List<Element> list = root.elements();
		for(Element e : list){
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}
	
	/**
	 * 文本消息对象转为xml
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToXml(TextMessage textMessage){
		XStream xStream = new XStream();
		xStream.alias("xml", textMessage.getClass());
		return xStream.toXML(textMessage);
	}
	
	/**
	 * 图文消息对象转为xml
	 * @param newsMessage
	 * @return
	 */
	public static String newsMessageToXml(NewsMessage newsMessage){
		XStream xStream = new XStream();
		xStream.alias("xml", newsMessage.getClass());
		xStream.alias("item", new News().getClass());
		return xStream.toXML(newsMessage);
	}
	
	/**
	 * 图片消息转为xml
	 * @param imageMessage
	 * @return
	 */
	public static String imageMessageToXml(ImageMessage imageMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}
	
	/**
	 * 音乐消息转为xml
	 * @param musicMessage
	 * @return
	 */
	public static String musicMessageToXml(MusicMessage musicMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}
	
	/**
	 * 初始化文本
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	public static String initText(String toUserName,String fromUserName,String content){
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(MessageUtil.MESSAGE_TEXT);
		text.setCreateTime(DateUtil.getCurrentDate().getTime());
		text.setContent(content);
		String message = textMessageToXml(text);
		return message;
	}
	
	/**
	 * 初始化图文消息
	 * @param toUserName
	 * @param fromUserName
	 * @param newsList
	 * @return
	 */
	public static String initNewsMessage(String toUserName,String fromUserName,List<News> newsList){
		String message = null;
		NewsMessage newsMessage = new NewsMessage();
		newsMessage.setFromUserName(toUserName);
		newsMessage.setToUserName(fromUserName);
		newsMessage.setMsgType(MessageUtil.MESSAGE_NEWS);
		newsMessage.setCreateTime(DateUtil.getCurrentDate().getTime());
		newsMessage.setArticleCount(newsList.size());
 		newsMessage.setArticles(newsList);
		message = newsMessageToXml(newsMessage);
		return message;
	}
	
	/**
	 * 初始化图片消息
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initImageMessage(String toUserName,String fromUserName,Image image){
		String message = null;
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setFromUserName(toUserName);
		imageMessage.setToUserName(fromUserName);
		imageMessage.setMsgType(MESSAGE_IMAGE);
		imageMessage.setCreateTime(DateUtil.getCurrentDate().getTime());
		imageMessage.setImage(image);
		message = imageMessageToXml(imageMessage);
		return message;
	}
	
	/**
	 * 初始化音乐消息
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initMusicMessage(String toUserName,String fromUserName,Music music){
		String message = null;
		MusicMessage musicMessage = new MusicMessage();
		musicMessage.setFromUserName(toUserName);
		musicMessage.setToUserName(fromUserName);
		musicMessage.setMsgType(MESSAGE_MUSIC);
		musicMessage.setCreateTime(DateUtil.getCurrentDate().getTime());
		musicMessage.setMusic(music);
		message = musicMessageToXml(musicMessage);
		return message;
	}
	
	/**
	 * 主菜单
	 * @return
	 */
	public static String menuText(){
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎您的关注，请按照菜单提示进行操作，如果遇到问题请发送邮件至harrysa66@163.com：\n\n");
		sb.append("1、机器人聊天\n");
		sb.append("2、查询天气\n");
		sb.append("3、查询图文天气\n");
		sb.append("4、查询快递（不支持语音）\n");
		sb.append("5、查询景点\n");
		sb.append("6、翻译\n");
		sb.append("7、周边搜索\n");
		sb.append("8、猜数字游戏\n\n");
		sb.append("9、查询交通\n\n");
		sb.append("回复?调出此菜单，更多功能敬请期待");
		return sb.toString();
	}
	
	/**
	 * 天气预报信息
	 * @return
	 */
	public static String weatherMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("进入查询天气模式，请输入“城市名称”进行查询，例如“天津”\n\n");
		sb.append("回复?退出此模式并调出主菜单");
		return sb.toString();
	}
	
	/**
	 * 天气预报查询结果
	 * @param city
	 * @return
	 */
	public static String weatherInfo(String city){
		StringBuffer sb = new StringBuffer();
		Weather weather = null;
		try {
			weather = WeatherUtil.getWeather(city);
			if(weather.getStatus().equals(WeatherUtil.ERROR_NO_RESULT)){
				sb.append("请输入正确的城市名称！");
			}else if(weather.getStatus().equals(WeatherUtil.ERROR_OK)){
				sb.append("当前时间：");
				sb.append(weather.getDate()).append("\n");
				sb.append(weather.getCurrentCity()).append("天气预报\n\n");
				sb.append(weather.getWeatherDataList().get(0).getDate()).append("\n");
				sb.append(weather.getWeatherDataList().get(0).getWeather()).append("\t");
				sb.append(weather.getWeatherDataList().get(0).getWind()).append("\t");
				sb.append(weather.getWeatherDataList().get(0).getTemperature()).append("\t");
				sb.append("PM2.5：").append(weather.getPm25()).append("\n\n");
				for(Index index : weather.getIndexList()){
					sb.append(index.getTipt()).append("：").append(index.getZs()).append("\n");
					sb.append(index.getDes()).append("\n\n");
				}
				for(int i = 1 ; i < weather.getWeatherDataList().size() ; i++){
					sb.append(weather.getWeatherDataList().get(i).getDate()).append("\n");
					sb.append(weather.getWeatherDataList().get(i).getWeather()).append("\t");
					sb.append(weather.getWeatherDataList().get(i).getWind()).append("\t");
					sb.append(weather.getWeatherDataList().get(i).getTemperature()).append("\n\n");
				}
			}else{
				sb.append("查询失败！");
			}
		} catch (IOException e) {
			sb.append("查询失败！");
		}
		return sb.toString();
	}
	
	/**
	 * 图文天气预报信息
	 * @return
	 */
	public static String newsWeatherMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("进入查询图文天气模式，请输入“城市名称”进行查询，例如“天津”\n\n");
		sb.append("回复?退出此模式并调出主菜单");
		return sb.toString();
	}
	
	/**
	 * 图文天气预报查询结果
	 * @param toUserName
	 * @param fromUserName
	 * @param city
	 * @return
	 */
	public static String newsWeatherInfo(String toUserName,String fromUserName,String city){
		String message = null;
		List<News> newsList = new ArrayList<News>();
		Weather weather = null;
		News news = null;
		StringBuffer sb = null;
		try {
			weather = WeatherUtil.getWeather(city);
			if(weather.getStatus().equals(WeatherUtil.ERROR_NO_RESULT)){
				message = initText(toUserName, fromUserName, "请输入正确的城市名称！");
			}else if(weather.getStatus().equals(WeatherUtil.ERROR_OK)){
				news = new News();
				sb = new StringBuffer();
				sb.append("当前时间：").append(weather.getDate()).append("\n");
				sb.append(weather.getCurrentCity()).append("天气预报");
				news.setTitle(sb.toString());
				newsList.add(news);
				
				news = new News();
				sb = new StringBuffer();
				sb.append(weather.getWeatherDataList().get(0).getDate()).append("\n");
				sb.append(weather.getWeatherDataList().get(0).getWeather()).append("\t");
				sb.append(weather.getWeatherDataList().get(0).getWind()).append("\t");
				sb.append(weather.getWeatherDataList().get(0).getTemperature()).append("\t");
				sb.append("PM2.5：").append(weather.getPm25());
				news.setTitle(sb.toString());
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
				Calendar cal = Calendar.getInstance();
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				if(hour >5 && hour <18){
					news.setPicUrl(weather.getWeatherDataList().get(0).getDayPictureUrl());
				}else{
					news.setPicUrl(weather.getWeatherDataList().get(0).getNightPictureUrl());
				}
				newsList.add(news);
				
				news = new News();
				sb = new StringBuffer();
				for(Index index : weather.getIndexList()){
					
					sb.append(index.getTipt()).append("：").append(index.getZs()).append("\n");
					sb.append(index.getDes()).append("\n\n");
				}
				news.setTitle(sb.toString());
				newsList.add(news);
				
				for(int i = 1 ; i < weather.getWeatherDataList().size() ; i++){
					news = new News();
					sb = new StringBuffer();
					sb.append(weather.getWeatherDataList().get(i).getDate()).append("\n");
					sb.append(weather.getWeatherDataList().get(i).getWeather()).append("\t");
					sb.append(weather.getWeatherDataList().get(i).getWind()).append("\t");
					sb.append(weather.getWeatherDataList().get(i).getTemperature());
					news.setTitle(sb.toString());
					news.setPicUrl(weather.getWeatherDataList().get(i).getDayPictureUrl());
					newsList.add(news);
				}
				message = initNewsMessage(toUserName, fromUserName, newsList);
			}else{
				message = initText(toUserName, fromUserName, "查询失败！");
			}
		} catch (IOException e) {
			message = initText(toUserName, fromUserName, "查询失败！");
		}
		return message;
	}
	
	/**
	 * 快递单号信息
	 * @return
	 */
	public static String expressMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("进入查询快递模式，请输入“快递名称,快递单号”进行查询，例如“汇通,350342680333”\n\n");
		sb.append("回复?退出此模式并调出主菜单");
		return sb.toString();
	}
	
	/**
	 * 快递单号查询结果
	 * @param param
	 * @return
	 */
	public static String expressInfo(String content){
		StringBuffer sb = new StringBuffer();
		content=content.replaceAll("，", ",");
		String[] contents = content.split(",");
		if(contents.length == 2){
			String expName = contents[0];
			String nu = contents[1];
			try {
				Express express = ExpressUtil.getExpressInfo(expName, nu);
				if(express.getStatus().equals("-2")){//查询失败
					sb.append(express.getMsg());
				}else if(express.getStatus().equals("-1")){
					sb.append("待查询");
				}else if(express.getStatus().equals("0")){
					sb.append("查询异常");
				}else if(express.getStatus().equals("1")){
					sb.append("暂无记录");
				}else if(!"".equals(express.getStatus())){//成功查询
					if(null != express.getMsg() && !"".equals(express.getMsg())){
						sb.append(express.getMsg());
					}else{
						sb.append(express.getExpTextName()).append("\n");
						sb.append("单号：").append(express.getMailNo()).append("\n");
						sb.append("更新时间：").append(express.getUpdate()).append("\n");
						sb.append("联系电话：").append(express.getTel()).append("\n\n");
						sb.append("物流信息：\n");
						for(ExpressData data : express.getExpressDataList()){
							sb.append(data.getTime()).append("\t").append(data.getContext()).append("\n");
						}
					}
				}else{
					sb.append("查询失败！");
				}
			} catch (IOException e) {
				sb.append("查询失败！");
			}
		}else{
			sb.append("请输入正确的查询格式！");
		}
		return sb.toString();
	}
	
	/**
	 * 景点查询信息
	 * @return
	 */
	public static String travelAttractionMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("进入查询景点模式，请输入“景点名称”进行查询，例如“颐和园”\n\n");
		sb.append("回复?退出此模式并调出主菜单");
		return sb.toString();
	}
	
	/**
	 * 景点查询结果
	 * @param toUserName
	 * @param fromUserName
	 * @param taName 中文名称
	 * @return
	 */
	public static String travelAttractionInfo(String toUserName,String fromUserName,String taName){
		String message = null;
		List<News> newsList = new ArrayList<News>();
		TravelAttraction travelAttraction = null;
		News news = null;
		StringBuffer sb = null;
		try {
			travelAttraction = TravelAttractionUtil.getTravelAttractionInfo(taName);
			if(travelAttraction.getStatus().equals(TravelAttractionUtil.ERROR_NO_RESULT)){
				message = initText(toUserName, fromUserName, "请输入正确的景点名称或景点不存在！");
			}else if(travelAttraction.getStatus().equals(TravelAttractionUtil.ERROR_OK)){
				news = new News();
				sb = new StringBuffer();
				sb.append(travelAttraction.getName());
				news.setTitle(sb.toString());
				news.setDescription(travelAttraction.getAbstracts());
				news.setPicUrl(TravelAttractionUtil.LOCATION_PIC+"center="+travelAttraction.getLng()+","+travelAttraction.getLat());
				news.setUrl(travelAttraction.getUrl());
				newsList.add(news);
				
				news = new News();
				sb = new StringBuffer();
				sb.append("简介：").append(travelAttraction.getDescription());
				news.setTitle(sb.toString());
				news.setUrl(travelAttraction.getUrl());
				newsList.add(news);
				
				if(null != travelAttraction.getPrice()){
					news = new News();
					sb = new StringBuffer();
					sb.append("票价：").append(travelAttraction.getPrice());
					news.setTitle(sb.toString());
					news.setUrl(travelAttraction.getUrl());
					newsList.add(news);
				}
				
				if(null != travelAttraction.getOpenTime()){
					news = new News();
					sb = new StringBuffer();
					sb.append("开放时间：").append(travelAttraction.getOpenTime());
					news.setTitle(sb.toString());
					news.setUrl(travelAttraction.getUrl());
					newsList.add(news);
				}
				
				if(null != travelAttraction.getAttentionList()){
					news = new News();
					sb = new StringBuffer();
					sb.append("更多事项：").append("\n");
					for(Attention attention : travelAttraction.getAttentionList()){
						sb.append(attention.getName()).append("\n");
						sb.append(attention.getDescription()).append("\n");
					}
					news.setTitle(sb.toString());
					newsList.add(news);
				}
				
				message = initNewsMessage(toUserName, fromUserName, newsList);
			}else{
				message = initText(toUserName, fromUserName, "查询失败！");
			}
		} catch (IOException e) {
			message = initText(toUserName, fromUserName, "查询失败！");
		}
		return message;
	}
	
	/**
	 * 翻译信息
	 * @return
	 */
	public static String transMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("进入翻译模式，请输入“需翻译的内容”进行翻译，例如“家庭”，“family”，“我们是一家人”，“we are family”\n\n");
		sb.append("回复?退出此模式并调出主菜单");
		return sb.toString();
	}
	
	/**
	 * 翻译结果
	 * @param source
	 * @return
	 */
	public static String transInfo(String source){
		String message = null;
		try {
			message = TransUtil.translate(source);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	/**
	 * 聊天信息
	 * @return
	 */
	public static String robotMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("进入聊天模式，直接发消息便可与小R机器人进行聊天，它可以陪你聊天并且具有很多功能等待你发现\n\n");
		sb.append("回复?退出此模式并调出主菜单");
		return sb.toString();
	}
	
	/**
	 * 聊天信息结果
	 * @param toUserName
	 * @param fromUserName
	 * @param info
	 * @return
	 * @throws IOException
	 */
	public static String robotInfo(String toUserName,String fromUserName,String info) throws IOException{
		String message = null;
		List<News> newsList = null;
		News news = null;
		StringBuffer sb = null;
		Robot<Object> robot = RobotUtil.getRobotInfo(fromUserName,info);
		if(null != robot.getCode() && !"".equals(robot.getCode())){
			if(robot.getCode().equals(RobotUtil.TEXT_CODE)){
				sb = new StringBuffer();
				sb.append(robot.getText());
				message = initText(toUserName, fromUserName, sb.toString());
			}else if(robot.getCode().equals(RobotUtil.URL_CODE)){
				newsList = new ArrayList<News>();
				news = new News();
				news.setTitle(robot.getText()+"，请点击查看。");
				news.setUrl(robot.getUrl());
				newsList.add(news);
				message = initNewsMessage(toUserName, fromUserName, newsList);
			}else if(robot.getCode().equals(RobotUtil.NEWS_CODE)){
				newsList = new ArrayList<News>();
				news = new News();
				news.setTitle(robot.getText());
				news.setPicUrl(RobotUtil.NEWS_IMAGE);
				newsList.add(news);
				
				com.product.robot.po.News[] news_r = (com.product.robot.po.News[]) robot.getList();
				for(int i = 0 ; i< 9 ; i++){
					news = new News();
					news.setTitle(news_r[i].getArticle()+"\n"+news_r[i].getSource());
					news.setPicUrl(news_r[i].getIcon());
					news.setUrl(news_r[i].getDetailurl());
					newsList.add(news);
					if(i == news_r.length - 1){
						break;
					}
				}
				message = initNewsMessage(toUserName, fromUserName, newsList);
			}else if(robot.getCode().equals(RobotUtil.TRAIN_CODE)){
				newsList = new ArrayList<News>();
				news = new News();
				news.setTitle(robot.getText());
				news.setPicUrl(RobotUtil.TRAIN_IMAGE);
				newsList.add(news);
				
				Train[] train = (Train[]) robot.getList();
				for(int i = 0 ; i< 9 ; i++){
					news = new News();
					sb = new StringBuffer();
					sb.append(train[i].getTrainnum()).append("\n");
					sb.append(train[i].getStart()).append("\t-\t").append(train[i].getTerminal()).append("\n");
					sb.append(train[i].getStarttime()).append("\t-\t").append(train[i].getEndtime());
					news.setTitle(sb.toString());
					news.setPicUrl(train[i].getIcon());
					news.setUrl(train[i].getDetailurl());
					newsList.add(news);
					if(i == train.length - 1){
						break;
					}
				}
				message = initNewsMessage(toUserName, fromUserName, newsList);
			}else if(robot.getCode().equals(RobotUtil.FLIGHT_CODE)){
				newsList = new ArrayList<News>();
				news = new News();
				news.setTitle(robot.getText());
				news.setPicUrl(RobotUtil.FLIGHT_IMAGE);
				newsList.add(news);
				
				Flight[] flight = (Flight[]) robot.getList();
				for(int i = 0 ; i< 9 ; i++){
					news = new News();
					sb = new StringBuffer();
					sb.append(flight[i].getFlight()).append("\n");
					sb.append(flight[i].getRoute()).append("\n");
					sb.append(flight[i].getStarttime()).append("\t-\t").append(flight[i].getEndtime()).append("\n");
					sb.append("航班状态：").append(flight[i].getState());
					news.setTitle(sb.toString());
					news.setPicUrl(flight[i].getIcon());
					news.setUrl(flight[i].getDetailurl());
					newsList.add(news);
					if(i == flight.length - 1){
						break;
					}
				}
				message = initNewsMessage(toUserName, fromUserName, newsList);
			}else if(robot.getCode().equals(RobotUtil.COOK_CODE)){
				newsList = new ArrayList<News>();
				news = new News();
				news.setTitle(robot.getText());
				news.setPicUrl(RobotUtil.COOK_IMAGE);
				newsList.add(news);
				
				CookBook[] cook = (CookBook[]) robot.getList();
				for(int i = 0 ; i< 9 ; i++){
					news = new News();
					sb = new StringBuffer();
					sb.append(cook[i].getName()).append("\n");
					sb.append(cook[i].getInfo());
					news.setTitle(sb.toString());
					news.setPicUrl(cook[i].getIcon());
					news.setUrl(cook[i].getDetailurl());
					newsList.add(news);
					if(i == cook.length - 1){
						break;
					}
				}
				message = initNewsMessage(toUserName, fromUserName, newsList);
			}else{
				sb = new StringBuffer();
				sb.append(robot.getText());
				message = initText(toUserName, fromUserName, sb.toString());
			}
		}else{
			message = initText(toUserName, fromUserName, "聊天失败，请输入正确信息！");
		}
		return message;
	}
	
	/**
	 * 交通事件信息
	 * @return
	 */
	public static String trafficMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("进入查询交通模式，请输入“城市名称”进行查询，例如“天津”，也可以发送地理位置信息进行查询。（因为微信字数限制，只显示部分信息）\n\n");
		sb.append("回复?退出此模式并调出主菜单");
		return sb.toString();
	}
	
	/**
	 * 交通事件结果
	 * @param location
	 * @return
	 */
	public static String trafficInfo(String location){
		String message = null;
		try {
			message = TrafficUtil.getTrafficEvent(location);
			System.out.println(message);
			System.out.println(message.getBytes("utf-8").length);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * 周边搜索信息
	 * @return
	 */
	public static String surroundingSearchMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("您是否有过出门在外四处找ATM或厕所的经历？").append("\n");
		sb.append("您是否有过出差在外搜寻美食或娱乐场所的经历？").append("\n");
		sb.append("周边搜索为您的出行保驾护航，为您提供专业的周边生活指南，发送位置开始体验吧！").append("\n\n");
		sb.append("周边搜索使用说明").append("\n");
		sb.append("1）发送地理位置").append("\n");
		sb.append("点击窗口底部的“+”按钮，选择“位置”，点“发送”").append("\n");
		sb.append("2）指定关键词搜索").append("\n");
		sb.append("格式：关键词\n例如：酒店、银行").append("\n\n");
		sb.append("回复?退出此模式并调出主菜单");
		return sb.toString();
	}

	/**
	 * 周边搜索结果
	 * @param location
	 * @return
	 */
	public static String surroundingSearchInfo(String toUserName,String fromUserName,String content){
		String message = null;
		try {
			message = SurroundingSearchUtil.getSearchResult(toUserName,fromUserName,content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * 接收并保存位置信息
	 * @param toUserName
	 * @param fromUserName
	 * @param lng
	 * @param lat
	 * @return
	 */
	public static String surroundingSearchLocation(String toUserName,String fromUserName,String lng,String lat){
		String message = null;
		try {
			message = SurroundingSearchUtil.saveSearchLocation(toUserName,fromUserName,lng,lat);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * 猜数字游戏信息
	 * @return
	 */
	public static String guessNumberMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("《猜数字游戏玩法》").append("\n");
		sb.append("系统设定一个没有重复数字的n（n ≥ 1）位数，由玩家来猜，每局10次机会。").append("\n");
		sb.append("每猜一次，系统会给出猜测结果xAyB，x表示数字与位置均正确的数的个数，y表示数字正确但位置不对的数的个数。").append("\n");
		sb.append("玩家根据猜测结果xAyB一直猜，直到猜中(nA0B)为止。").append("\n");
		sb.append("如果10次都没猜中，系统会公布答案，游戏结束。").append("\n");
		sb.append("玩家任意输入一个没有重复数字的n（n ≥ 1）位数即开始游戏，例如：7890").append("\n\n");
		sb.append("回复?退出此模式并调出主菜单");
		return sb.toString();
	}

	/**
	 * 进行猜数字游戏
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	public static String guessNumberInfo(String toUserName, String fromUserName, String content) {
		String message = null;
		try {
			message = GuessNumberUtil.processGame(toUserName, fromUserName, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * 失效菜单的返回信息
	 * @return
	 */
	public static String invalidMenuMessage(){
		StringBuffer sb = new StringBuffer();
		sb.append("此功能已失效，请使用其他功能。");
		return sb.toString();
	}

	/**
	 * 提示正确信息的返回信息
	 * @return
	 */
	public static String correctTipsMessage(){
		StringBuffer sb = new StringBuffer();
		sb.append("请按照菜单提示发送正确信息，回复?调出主菜单！");
		return sb.toString();
	}

	/**
	 * 不支持消息的返回信息
	 * @return
	 */
	public static String notSupportMessage(){
		StringBuffer sb = new StringBuffer();
		sb.append("暂不支持该信息！");
		return sb.toString();
	}

	/**
	 * 出现问题的返回信息
	 * @return
	 */
	public static String problemMessage(){
		StringBuffer sb = new StringBuffer();
		sb.append("出现问题，请重新关注此微信号！");
		return sb.toString();
	}
	
	/**
	 * 其他信息
	 * @return
	 */
	public static String otherMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("这里是其他信息测试...");
		return sb.toString();
	}

}
