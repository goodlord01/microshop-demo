package com.microshop.domain;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.util.Date;

/**
 * Created by Admin on 11/7/2016.
 */
@Entity
@Table(name = "thumbs")
public class Thumbs {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "sellers_id")
    private Integer sellersId;

    @Column(name = "sellers_openid")
    private String sellersOpenid;

    @Column(name = "openid")
    private String openid;

    @Column(name = "created_datetime")
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

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Date getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Date createdDatetime) {
        this.createdDatetime = createdDatetime;
    }
}