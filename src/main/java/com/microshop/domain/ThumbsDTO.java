package com.microshop.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;

import java.util.Date;

/**
 * Created by Admin on 11/8/2016.
 */
public class ThumbsDTO {

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "sellers_id")
    private Integer sellersId;

    @JsonProperty(value = "sellers_openid")
    private String sellersOpenid;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "age")
    private Integer age;

    @JsonProperty(value = "sex")
    private Boolean sex;

    @JsonProperty(value = "province")
    private String province;

    @JsonProperty(value = "city")
    private String city;

    @JsonProperty(value = "address")
    private String address;

    @JsonProperty(value = "openid")
    private String openid;

    @JsonProperty(value = "gravatar_url")
    private String gravatarUrl;

    @JsonProperty(value = "created_datetime")
    private Date createdDatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSellersId() {
        return sellersId;
    }

    public void setSellersId(Integer sellersId) {
        this.sellersId = sellersId;
    }

    public String getSellersOpenid() {
        return sellersOpenid;
    }

    public void setSellersOpenid(String sellersOpenid) {
        this.sellersOpenid = sellersOpenid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getGravatarUrl() {
        return gravatarUrl;
    }

    public void setGravatarUrl(String gravatarUrl) {
        this.gravatarUrl = gravatarUrl;
    }

    public Date getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Date createdDatetime) {
        this.createdDatetime = createdDatetime;
    }
}
