package com.microshop.customers;

import com.microshop.domain.Customer;
import com.microshop.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by min on 25/10/2016.
 */

@Service
@Transactional
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;


    public Customer createCustomer(Customer customer) {
        customer.setCreatedDatetime(new Date());
        return customerRepository.save(customer);
    }

    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public Customer getByOpenId(String openId){
        return customerRepository.findOne(openId);
    }

}
