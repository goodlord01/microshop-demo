package com.microshop.customers;

import com.microshop.domain.Customer;
import com.microshop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by min on 25/10/2016.
 */

public interface CustomerRepository extends JpaRepository<Customer, String> {

}
