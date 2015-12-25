package com.weixin.servlet;

import com.weixin.constant.FlagConstant;
import com.weixin.dao.UserDao;
import com.weixin.po.User;
import com.weixin.util.CheckUtil;
import com.weixin.util.MessageUtil;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class WeixinServlet extends HttpServlet{
	
	static Logger log = Logger.getLogger(WeixinServlet.class.getName());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2836724667530087285L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		PrintWriter out = resp.getWriter();
		if(CheckUtil.checkSignature(signature, timestamp, nonce)){
			out.print(echostr);
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		UserDao userDao = new UserDao();
		String flag = null;
		try {
			Map<String, String> map = MessageUtil.xmlToMap(req);
			String toUserName = map.get("ToUserName");
			String fromUserName = map.get("FromUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			
			User user = userDao.getUser(fromUserName);
			String message = null;
			if(null != user){
				flag = user.getFlag();
				if(MessageUtil.MESSAGE_TEXT.equals(msgType)){//文本消息
					log.info("weixin_message_"+msgType+"---->"+content);
					//进入功能
					if(FlagConstant.ROBOT.equals(content) && flag.equals(FlagConstant.INIT)){//聊天
						userDao.setFlag(fromUserName, FlagConstant.ROBOT);
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.robotMenu());
					}else if(FlagConstant.WEATHER.equals(content) && flag.equals(FlagConstant.INIT)){//天气
						userDao.setFlag(fromUserName, FlagConstant.WEATHER);
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.weatherMenu());
					}else if(FlagConstant.NEWS_WEATHER.equals(content) && flag.equals(FlagConstant.INIT)){//图文天气
						userDao.setFlag(fromUserName, FlagConstant.NEWS_WEATHER);
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.newsWeatherMenu());
					}else if(FlagConstant.EXPRESS.equals(content) && flag.equals(FlagConstant.INIT)){//快递
						userDao.setFlag(fromUserName, FlagConstant.EXPRESS);
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.expressMenu());
					}else if(FlagConstant.TRAVEL_ATTRACTION.equals(content) && flag.equals(FlagConstant.INIT)){//景点
						userDao.setFlag(fromUserName, FlagConstant.TRAVEL_ATTRACTION);
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.travelAttractionMenu());
					}else if(FlagConstant.TRANS.equals(content) && flag.equals(FlagConstant.INIT)){//翻译
						userDao.setFlag(fromUserName, FlagConstant.TRANS);
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.transMenu());
					}else if(FlagConstant.SURROUNDING_SEARCH.equals(content) && flag.equals(FlagConstant.INIT)){//周边搜索
						userDao.setFlag(fromUserName, FlagConstant.SURROUNDING_SEARCH);
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.surroundingSearchMenu());
					}/*else if(FlagConstant.TRAFFIC.equals(content) && flag.equals(FlagConstant.INIT)){//交通
						userDao.setFlag(fromUserName, FlagConstant.TRAFFIC);
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.trafficMenu());
					}*/
					//使用功能
					else if(FlagConstant.MENU_EN.equals(content) || FlagConstant.MENU_ZH.equals(content)){//主菜单
						userDao.setFlag(fromUserName, FlagConstant.INIT);
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
					}else if(flag.equals(FlagConstant.ROBOT)){//聊天
						message = MessageUtil.robotInfo(toUserName, fromUserName, content);
					}else if(flag.equals(FlagConstant.WEATHER)){//天气
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.weatherInfo(content));
					}else if(flag.equals(FlagConstant.NEWS_WEATHER)){//图文天气
						message = MessageUtil.newsWeatherInfo(toUserName, fromUserName, content);
					}else if(flag.equals(FlagConstant.EXPRESS)){//快递
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.expressInfo(content));
					}else if(flag.equals(FlagConstant.TRAVEL_ATTRACTION)){//景点
						message = MessageUtil.travelAttractionInfo(toUserName, fromUserName, content);
					}else if(flag.equals(FlagConstant.TRANS)){//翻译
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.transInfo(content));
					}else if(flag.equals(FlagConstant.SURROUNDING_SEARCH)){//周边搜索
						message = MessageUtil.surroundingSearchInfo(toUserName, fromUserName, content);
					}/*else if(flag.equals(FlagConstant.TRAFFIC)){//交通
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.trafficInfo(content));
					}*/else{
						message = MessageUtil.initText(toUserName, fromUserName, "请按照菜单提示发送正确信息，回复?调出主菜单！");
					}
				}else if(MessageUtil.MESSAGE_VOICE.equals(msgType)){//语音消息
					String recognition = map.get("Recognition");
					log.info("weixin_message_"+msgType+"---->"+recognition);
					if(flag.equals(FlagConstant.ROBOT)){
						message = MessageUtil.robotInfo(toUserName, fromUserName, recognition);
					}else if(flag.equals(FlagConstant.WEATHER)){
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.weatherInfo(recognition));
					}else if(flag.equals(FlagConstant.NEWS_WEATHER)){
						message = MessageUtil.newsWeatherInfo(toUserName, fromUserName, recognition);
					}else if(flag.equals(FlagConstant.TRAVEL_ATTRACTION)){
						message = MessageUtil.travelAttractionInfo(toUserName, fromUserName, recognition);
					}else if(flag.equals(FlagConstant.TRANS)){
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.transInfo(recognition));
					}else if(flag.equals(FlagConstant.SURROUNDING_SEARCH)){
						message = MessageUtil.surroundingSearchInfo(toUserName, fromUserName, recognition);
					}/*else if(flag.equals(FlagConstant.TRAFFIC)){
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.trafficInfo(recognition));
					}*/else{
						message = MessageUtil.initText(toUserName, fromUserName, "请按照菜单提示发送语音信息，回复?调出主菜单！");
					}
				}else if(MessageUtil.MESSAGE_LOCATION.equals(msgType)){
					String lng = map.get("Location_Y");
					String lat = map.get("Location_X");
					/*if(flag.equals(FlagConstant.TRAFFIC)){//交通
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.trafficInfo(loY+","+loX));
					}*/
					if(flag.equals(FlagConstant.SURROUNDING_SEARCH)){//周边搜索
						message =  MessageUtil.surroundingSearchLocation(toUserName, fromUserName,lng,lat);
					}
				}else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){//事件推送消息
					String eventType = map.get("Event");
					log.info("weixin_message_"+msgType+"---->"+eventType);
					if(MessageUtil.MESSAGE_EVENT_UNSUBSCRIBE.equals(eventType)){
						userDao.deleteUser(fromUserName);
					}
				}else{
					message = MessageUtil.initText(toUserName, fromUserName, "暂不支持该信息！");
				}
			}else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){//事件推送消息
				String eventType = map.get("Event");
				if(MessageUtil.MESSAGE_EVENT_SUBSCRIBE.equals(eventType)){
					userDao.insertUser(fromUserName, toUserName);
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else{
					message = MessageUtil.initText(toUserName, fromUserName, "出现问题，请重新关注此微信号！");
				}
			}else{
				message = MessageUtil.initText(toUserName, fromUserName, "出现问题，请重新关注此微信号！");
			}
			out.print(message);
		} catch (DocumentException e) {
			e.printStackTrace();
		}finally{
			out.close();
		}
	}

}
