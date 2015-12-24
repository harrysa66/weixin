package com.product.surroundingSearch.dao;

import com.product.surroundingSearch.po.UserLocation;
import com.weixin.dao.BaseDao;
import com.weixin.util.DateUtil;

/**
 * Created by harrysa66 on 2015/12/24.
 */
public class UserLocationDao extends BaseDao {

    /**
     * 保存用户位置信息
     * @param openId
     * @param lng
     * @param lat
     * @param bd09Lng
     * @param bd09Lat
     * @return
     */
    public int saveUserLocation(String openId,String lng,String lat,String bd09Lng,String bd09Lat){
        Object[] params = new Object[6];
        params[0] = openId;
        params[1] = lng;
        params[2] = lat;
        params[3] = bd09Lng;
        params[4] = bd09Lat;
        params[5] = DateUtil.getCurrentDate();
        String sql = "insert into user_location(open_id,lng,lat,bd09_lng,bd09_lat,create_time) values(?,?,?,?,?,?)";
        int line = executeUpdate(sql, params);
        return line;
    }

    /**
     * 得到用户最后位置信息
     * @param openId
     * @return
     */
    public UserLocation getLastLocation(String openId){
        Object[] params = new Object[1];
        params[0] = openId;
        String sql = "select id,open_id,lng,lat,bd09_lng,bd09_lat,create_time from user_location where open_id = ? order by create_time desc limit 0,1";
        UserLocation userLocation = (UserLocation)executeQueryObject(sql,UserLocation.class,params);
        return userLocation;
    }

}
