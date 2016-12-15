package com.microshop.domain;

import javax.persistence.*;

/**
 * Created by min on 23/10/2016.
 */

@Entity
@Table(name="addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="address")
    private String address;

    @Column(name="name")
    private String name;

    @Column(name="phone")
    private String phone;

    @Column(name="city")
    private String city;

    @Column(name="province")
    private String province;

    @Column(name="country")
    private String country;

    @Column(name="zipcode")
    private String zipCode;

    @Column(name="openid")
    private String openId;

    public Address(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    private static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Address)) {
            return false;
        }

        Address that = (Address) other;

        return equal(this.address, that.address)
                && equal(this.name, that.name)
                && equal(this.phone, that.phone)
                && equal(this.city, that.city)
                && equal(this.province, that.province)
                && equal(this.country, that.country)
                && equal(this.zipCode, that.zipCode)
                && equal(this.openId, that.openId);

    }

    @Override
    public int hashCode() {
        int hashCode = 1;

        hashCode = hashCode * 37 + this.address.hashCode();
        hashCode = hashCode * 37 + this.name.hashCode();
        hashCode = hashCode * 37 + this.phone.hashCode();
        hashCode = hashCode * 37 + this.city.hashCode();
        hashCode = hashCode * 37 + this.province.hashCode();
        hashCode = hashCode * 37 + this.country.hashCode();
        hashCode = hashCode * 37 + this.zipCode.hashCode();
        hashCode = hashCode * 37 + this.openId.hashCode();

        return hashCode;
    }
}
