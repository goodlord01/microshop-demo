package com.microshop.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 11/14/2016.
 */
public class AddressDTO {

    private String name;

    private String phone;

    private String city;

    private String province;

    private String country;

    @JsonProperty("zip_code")
    private String zipCode;

    @JsonProperty("openid")
    private String openId;

    private String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AddressDTO(){}

    public AddressDTO(Address address){
        this.setProvince(address.getProvince());
        this.setCity(address.getCity());
        this.setCountry(address.getCountry());
        this.setZipCode(address.getZipCode());
        this.setAddress(address.getAddress());
        this.setPhone(address.getPhone());
        this.setName(address.getName());
        this.setOpenId(address.getOpenId());
    }

    public Address toAddress(){
        if(this == null)
            return null;

        Address address = new Address();
        address.setProvince(this.getProvince());
        address.setCity(this.getCity());
        address.setCountry(this.getCountry());
        address.setZipCode(this.getZipCode());
        address.setAddress(this.getAddress());
        address.setPhone(this.getPhone());
        address.setName(this.getName());
        address.setOpenId(this.getOpenId());
        return address;
    }
}
