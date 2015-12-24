package com.product.surroundingSearch.po;

import java.util.Date;

/**
 * Created by harrysa66 on 2015/12/24.
 */
public class UserLocation {

    private Integer id;
    private String openId;
    private String lng;
    private String lat;
    private String bd09Lng;
    private String bd09Lat;
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getBd09Lng() {
        return bd09Lng;
    }

    public void setBd09Lng(String bd09Lng) {
        this.bd09Lng = bd09Lng;
    }

    public String getBd09Lat() {
        return bd09Lat;
    }

    public void setBd09Lat(String bd09Lat) {
        this.bd09Lat = bd09Lat;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
