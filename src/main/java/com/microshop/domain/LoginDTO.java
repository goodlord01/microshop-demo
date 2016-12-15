package com.microshop.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 11/25/2016.
 */
public class LoginDTO {

    @JsonProperty("is_login")
    private boolean isLogin;

    @JsonProperty("is_seller")
    private boolean isSeller;

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setIsSeller(boolean isSeller) {
        this.isSeller = isSeller;
    }
}
