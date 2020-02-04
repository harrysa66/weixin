package com.weixin.servlet;

import com.weixin.constant.FlagConstant;
import com.weixin.dao.UserDao;
import com.weixin.po.User;
import com.weixin.util.CheckUtil;
import com.weixin.util.MessageUtil;
import com.weixin.util.PropertiesUtil;
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
				switch (msgType){
					case MessageUtil.MESSAGE_TEXT:// 文本消息
						log.info("weixin_message_"+msgType+"---->"+content);
						// 输入问号,返回初始菜单
						if (FlagConstant.MENU_EN.equals(content) || FlagConstant.MENU_ZH.equals(content)){
							userDao.setFlag(fromUserName, FlagConstant.INIT);
							message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
						} else {
							//进入功能
							switch (flag){
								case FlagConstant.INIT:// 初始化
									if (!isExist(content)){
										userDao.setFlag(fromUserName, FlagConstant.INIT);
										message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.correctTipsMessage());
									} else if (isInvalid(content)) {
										message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.invalidMenuMessage());
									} else {
										userDao.setFlag(fromUserName, content);
										message = enterMenu(toUserName, fromUserName, content);
									}
									break;
								case FlagConstant.ROBOT:// 聊天
									message = MessageUtil.robotInfo(toUserName, fromUserName, content);
									break;
								case FlagConstant.WEATHER:// 天气
									message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.weatherInfo(content));
									break;
								case FlagConstant.NEWS_WEATHER:// 图文天气
									message = MessageUtil.newsWeatherInfo(toUserName, fromUserName, content);
									break;
								case FlagConstant.EXPRESS:// 快递
									message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.expressInfo(content));
									break;
								case FlagConstant.TRAVEL_ATTRACTION:// 景点
									message = MessageUtil.travelAttractionInfo(toUserName, fromUserName, content);
									break;
								case FlagConstant.TRANS:// 翻译
									message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.transInfo(content));
									break;
								case FlagConstant.SURROUNDING_SEARCH:// 周边搜索
									message = MessageUtil.surroundingSearchInfo(toUserName, fromUserName, content);
									break;
								case FlagConstant.GUESS_NUMBER:// 猜数字游戏
									message = MessageUtil.guessNumberInfo(toUserName, fromUserName, content);
									break;
								case FlagConstant.TRAFFIC:// 交通
									message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.trafficInfo(content));
									break;
								default:
									userDao.setFlag(fromUserName, FlagConstant.INIT);
									message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.correctTipsMessage());
									break;

							}
						}
						break;
					case MessageUtil.MESSAGE_VOICE:// 语音消息
						String recognition = map.get("Recognition");
						log.info("weixin_message_"+msgType+"---->"+recognition);
						switch (flag){
							case FlagConstant.ROBOT:// 聊天
								message = MessageUtil.robotInfo(toUserName, fromUserName, recognition);
								break;
							case FlagConstant.WEATHER:// 天气
								message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.weatherInfo(recognition));
								break;
							case FlagConstant.NEWS_WEATHER:// 图文天气
								message = MessageUtil.newsWeatherInfo(toUserName, fromUserName, recognition);
								break;
							case FlagConstant.TRAVEL_ATTRACTION:// 景点
								message = MessageUtil.travelAttractionInfo(toUserName, fromUserName, recognition);
								break;
							case FlagConstant.TRANS:// 翻译
								message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.transInfo(recognition));
								break;
							case FlagConstant.SURROUNDING_SEARCH:// 周边搜索
								message = MessageUtil.surroundingSearchInfo(toUserName, fromUserName, recognition);
								break;
							case FlagConstant.TRAFFIC:// 交通
								message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.trafficInfo(recognition));
								break;
							default:
								message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.correctTipsMessage());
								break;
						}
						break;
					case MessageUtil.MESSAGE_LOCATION:// 地理位置消息
						String lng = map.get("Location_Y");
						String lat = map.get("Location_X");
						switch (flag){
							case FlagConstant.SURROUNDING_SEARCH:// 周边搜索
								message =  MessageUtil.surroundingSearchLocation(toUserName, fromUserName,lng,lat);
								break;
							case FlagConstant.TRAFFIC:// 交通
								message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.trafficInfo(lng+","+lat));
								break;
							default:
								userDao.setFlag(fromUserName, FlagConstant.INIT);
								message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
								break;
						}
						break;
					case MessageUtil.MESSAGE_EVENT:// 事件推送消息
						String eventType = map.get("Event");
						log.info("weixin_message_"+msgType+"---->"+eventType);
						if(MessageUtil.MESSAGE_EVENT_UNSUBSCRIBE.equals(eventType)){
							// 用户取消关注,删除该用户
							userDao.deleteUser(fromUserName);
						}
						break;
					default:
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.notSupportMessage());
						break;
				}
			}else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){//事件推送消息
				// 用户首次进入
				String eventType = map.get("Event");
				if(MessageUtil.MESSAGE_EVENT_SUBSCRIBE.equals(eventType)){
					// 用户已关注,创建用户
					userDao.insertUser(fromUserName, toUserName);
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else{
					// 用户未关注,提示用户关注此公众号
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.problemMessage());
				}
			}else{
				// 出现异常,提示用户关注此公众号
				message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.problemMessage());
			}
			out.print(message);
		} catch (DocumentException e) {
			e.printStackTrace();
		}finally{
			out.close();
		}
	}

	/**
	 * 进入菜单
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	private String enterMenu(String toUserName, String fromUserName, String content){
		String message = null;
		switch (content){
			case FlagConstant.ROBOT:// 聊天
				message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.robotMenu());
				break;
			case FlagConstant.WEATHER:// 天气
				message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.weatherMenu());
				break;
			case FlagConstant.NEWS_WEATHER:// 图文天气
				message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.newsWeatherMenu());
				break;
			case FlagConstant.EXPRESS:// 快递
				message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.expressMenu());
				break;
			case FlagConstant.TRAVEL_ATTRACTION://景点
				message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.travelAttractionMenu());
				break;
			case FlagConstant.TRANS:// 翻译
				message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.transMenu());
				break;
			case FlagConstant.SURROUNDING_SEARCH:// 周边搜索
				message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.surroundingSearchMenu());
				break;
			case FlagConstant.GUESS_NUMBER:// 猜数字游戏
				message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.guessNumberMenu());
				break;
			case FlagConstant.TRAFFIC:// 交通
				message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.trafficMenu());
				break;
			default:
				message = MessageUtil.initText(toUserName, fromUserName, "请按照菜单提示发送正确信息，回复?调出主菜单！");
				break;
		}
		return message;
	}

	/**
	 * 判断功能是否失效
	 * @param content
	 * @return
	 */
	private boolean isInvalid(String content){
		String invalidMenu = PropertiesUtil.getInstance().getString("invalidMenu");
		return invalidMenu.contains("|"+ content + "|");
	}

	/**
	 * 判断菜单是否存在
	 * @param content
	 * @return
	 */
	private boolean isExist(String content){
		String invalidMenu = PropertiesUtil.getInstance().getString("existMenu");
		return invalidMenu.contains("|"+ content + "|");
	}

}
