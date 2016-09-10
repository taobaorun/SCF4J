package com.jiaxy.cache.model;

import java.io.Serializable;

/**
 * @author: wutao
 * @version: $Id:DemoResult.java 2014/01/22 16:52 $
 *
 */
public class DemoResult implements Serializable {

    private String shopId;

    private String data;

    private String date;


    public DemoResult() {
    }

    public DemoResult(String shopId, String data, String date) {
        this.shopId = shopId;
        this.data = data;
        this.date = date;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
