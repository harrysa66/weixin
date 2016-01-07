package com.product.surroundingSearch.util;

import com.product.surroundingSearch.dao.UserLocationDao;
import com.product.surroundingSearch.po.BaiduPlace;
import com.product.surroundingSearch.po.UserLocation;
import com.weixin.po.message.News;
import com.weixin.util.BaiduMapUtil;
import com.weixin.util.MessageUtil;

import java.util.List;

/**
 * Created by harrysa66 on 2015/12/25.
 */
public class SurroundingSearchUtil {

    static UserLocationDao userLocationDao = new UserLocationDao();

    /**
     * 得到搜索结果
     * @param toUserName
     * @param fromUserName
     * @param content
     * @return
     * @throws Exception
     */
    public static String getSearchResult(String toUserName,String fromUserName,String content) throws Exception {
        String message = "";
        /*if (content.startsWith("附近")) {*/
            String keyWord = content./*replaceAll("附近", "").*/trim();
            // 获取用户最后一次发送的地理位置
            UserLocation location = userLocationDao.getLastLocation(fromUserName);
            // 未获取到
            if (null == location) {
                message = MessageUtil.initText(toUserName, fromUserName, getUsage());
            }else{
                //地理位置创建时间与当前时间相差分钟数
                Long s = (System.currentTimeMillis() - location.getCreateTime().getTime()) / (1000 * 60);
                // 根据转换后（纠偏）的坐标搜索周边POI
                List<BaiduPlace> placeList = BaiduMapUtil.searchPlace(keyWord, location.getBd09Lng(), location.getBd09Lat());
                if(s > 30){
                    message = MessageUtil.initText(toUserName, fromUserName, "/难过，当前位置信息已超过30分钟有效期，请重新发送位置信息！");
                }else{
                    // 未搜索到POI
                    if (null == placeList || 0 == placeList.size()) {
                        message = MessageUtil.initText(toUserName, fromUserName, String.format("/难过，您发送的位置附近未搜索到“%s”信息！", keyWord));
                    } else {
                        List<News> articleList = BaiduMapUtil.makeArticleList(placeList, location.getBd09Lng(), location.getBd09Lat());
                        // 回复图文消息
                        message = MessageUtil.initNewsMessage(toUserName, fromUserName, articleList);
                    }
                }
            }
        /*}else{
            message = MessageUtil.initText(toUserName, fromUserName, getUsage());
        }*/
        return message;
    }

    /**
     * 保存位置信息
     * @param toUserName
     * @param fromUserName
     * @param lng
     * @param lat
     * @return
     */
    public static String saveSearchLocation(String toUserName,String fromUserName,String lng,String lat){
        // 坐标转换后的经纬度
        String bd09Lng = null;
        String bd09Lat = null;
        // 调用接口转换坐标
        UserLocation userLocation = BaiduMapUtil.convertCoord(lng, lat);
        if (null != userLocation) {
            bd09Lng = userLocation.getBd09Lng();
            bd09Lat = userLocation.getBd09Lat();
        }
        // 保存用户地理位置
        userLocationDao.saveUserLocation(fromUserName, lng, lat, bd09Lng, bd09Lat);

        StringBuffer sb = new StringBuffer();
        sb.append("[愉快]").append("成功接收您的位置，位置信息的有效期为30分钟！").append("\n\n");
        sb.append("您可以输入搜索关键词获取周边信息了，例如：").append("\n");
        sb.append("        酒店").append("\n");
        sb.append("        银行").append("\n");
        return MessageUtil.initText(toUserName, fromUserName, sb.toString());
    }

    /**
     * 使用说明
     *
     * @return
     */
    public static String getUsage() {
        StringBuffer sb = new StringBuffer();
        sb.append("周边搜索使用说明").append("\n");
        sb.append("1）发送地理位置").append("\n");
        sb.append("点击窗口底部的“+”按钮，选择“位置”，点“发送”").append("\n");
        sb.append("2）指定关键词搜索").append("\n");
        sb.append("格式：关键词\n例如：酒店、银行").append("\n\n");
        return sb.toString();
    }
}
