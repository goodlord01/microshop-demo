package com.microshop.domain;

import com.microshop.utils.HashUtils;
import org.springframework.util.StringUtils;

/**
 * Created by yan on 11/15/2016.
 */
public class Authentication {

    private String openid;

    private String hash;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Authentication(){}

    public Authentication(String openid, String hash){
        this.openid = openid;
        this.hash = HashUtils.sha256HexString(openid);
    }

    public boolean check(){
        if(StringUtils.hasText(this.openid) && StringUtils.hasText(this.hash)) {
            String openIdHash = HashUtils.sha256HexString(this.openid);
            if (openIdHash.equals(this.hash))
                return true;
        }
        return false;

    }
}
