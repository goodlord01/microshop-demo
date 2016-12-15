package com.microshop.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;

/**
 * Created by yan on 11/22/2016.
 */
public class CustomerDTO {

    @JsonProperty("openid")
    private String openId;

    @JsonProperty("name")
    private String name;

    @Column(name = "gravatar_url")
    private String gravatarUrl;

    public CustomerDTO(){}

    public CustomerDTO(Customer customer){
        this.openId = customer.getOpenId();
        this.name = customer.getNickname();
        this.gravatarUrl = customer.getGravatarUrl();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGravatarUrl() {
        return gravatarUrl;
    }

    public void setGravatarUrl(String gravatarUrl) {
        this.gravatarUrl = gravatarUrl;
    }
}
