package com.microshop.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 11/14/2016.
 */
public class OrderResponse {

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("openid")
    private String openId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public OrderResponse(){}

    public OrderResponse(String orderId, String openId){
        this.openId = openId;
        this.orderId = orderId;
    }

}
