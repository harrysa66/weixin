package com.weixin.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
	
	/**
	 * 获取当前时间(北京时间)
	 * @return
	 */
	public static Date getCurrentDate(){
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		return date;
	}

}
