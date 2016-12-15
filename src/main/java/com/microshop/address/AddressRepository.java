package com.microshop.address;

import com.microshop.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by min on 25/10/2016.
 */
public interface AddressRepository extends JpaRepository<Address, Integer> {

    Address findById(Integer id);

    List<Address> findByOpenId(String openId);

}
