package com.microshop.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microshop.domain.Authentication;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yan on 11/14/2016.
 */
public class AuthUtils {

    public static String getOpenId(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("openid".equals(cookie.getName())) {
                    String value = cookie.getValue();
                    Authentication auth = parseCookieValue(value);
                    if (auth!=null && auth.check())
                        return auth.getOpenid();
                }
            }
        }

        return null;
    }

    public static void setOpenId(HttpServletResponse response, String openId, String hashSalt) {
        Authentication auth = new Authentication(openId, hashSalt);
        if (StringUtils.hasText(openId) && StringUtils.hasText(hashSalt)) {
            Cookie cookie = new Cookie("openid", generateCookieValue(auth));
            cookie.setMaxAge(30 * 24 * 3600); //30 days
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

    public static void setYSID(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        boolean hasYSID = false;
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if("YSID".equals(cookie.getName())){
                    hasYSID = true;
                    break;
                }
            }
        }
        if(!hasYSID){
            Cookie cookie = new Cookie("YSID", YSIDGenerator.getNew());
            cookie.setMaxAge(Integer.MAX_VALUE);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

    }

    private static String generateCookieValue(Authentication auth) {
        return "openid=" + auth.getOpenid() + "&hash=" + auth.getHash();
    }

    private static Authentication parseCookieValue(String value) {
        String[] valueList = value.split("&");
        if (valueList.length == 2) {
            if (StringUtils.hasText(valueList[0]) && StringUtils.hasText(valueList[1])) {
                String[] openIdVal = valueList[0].split("=");
                String[] hashVal = valueList[1].split("=");
                if (openIdVal.length == 2 && hashVal.length == 2) {
                    return new Authentication(openIdVal[1], hashVal[1]);
                }
            }
        }

        return null;
    }
}
