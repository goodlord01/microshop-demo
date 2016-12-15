package com.microshop.domain;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by min on 21/11/2016.
 */

@Entity
@Table(name="sellers")
public class Seller {

    @Id
    @Column(name="openid")
    private String openId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "sex")
    private Boolean sex;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "headimgurl")
    private String headingImgUrl;

    @OneToMany(mappedBy="seller")
    private Set<Product> products;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadingImgUrl() {
        return headingImgUrl;
    }

    public void setHeadingImgUrl(String headingImgUrl) {
        this.headingImgUrl = headingImgUrl;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
