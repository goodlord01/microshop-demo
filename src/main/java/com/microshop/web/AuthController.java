package com.microshop.web;

import com.microshop.domain.LoginDTO;
import com.microshop.domain.Seller;
import com.microshop.seller.SellerRepository;
import com.microshop.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yan on 11/25/2016.
 */
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private SellerRepository sellerRepository;

    @RequestMapping(value = "check", method = RequestMethod.GET)
    public boolean checkLogin(HttpServletRequest request){
        String openId = AuthUtils.getOpenId(request);
        if(StringUtils.hasText(openId))
            return true;

        return false;
    }

    @RequestMapping(value = "/seller_check", method = RequestMethod.GET)
    public LoginDTO checkSellerLogin(HttpServletRequest request){
        LoginDTO loginResult = new LoginDTO();
        String openId = AuthUtils.getOpenId(request);
        if(StringUtils.hasText(openId)){
            loginResult.setIsLogin(true);
            Seller seller = sellerRepository.findOne(openId);
            if(seller != null)
                loginResult.setIsSeller(true);
        }

        return loginResult;
    }
}
