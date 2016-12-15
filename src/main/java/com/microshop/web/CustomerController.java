package com.microshop.web;

import com.microshop.customers.CustomerService;
import com.microshop.domain.Customer;
import com.microshop.domain.CustomerDTO;
import com.microshop.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yan on 11/22/2016.
 */
@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping("current")
    public CustomerDTO getCurrentCustomer(HttpServletRequest request, HttpServletResponse response){
        String openId = AuthUtils.getOpenId(request);
        if(StringUtils.hasText(openId))
            return new CustomerDTO(customerService.getByOpenId(openId));

        AuthUtils.setYSID(request, response);
        return null;
    }
}
