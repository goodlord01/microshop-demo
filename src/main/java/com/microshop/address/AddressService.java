package com.microshop.address;

import com.microshop.domain.Address;
import com.microshop.domain.AddressDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by min on 13/11/2016.
 */

@Service
public class AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    @Autowired
    AddressRepository addressRepo;

    public Address createAddress(AddressDTO address)
    {
        Address savedAddress = addressRepo.save(address.toAddress());
        logger.info("New address created. Address id : ", savedAddress.getId());
        return savedAddress;
    }

    public Address isAddressExist(Address address) {
        List<Address> addresses = addressRepo.findByOpenId(address.getOpenId());
        for (Address obj : addresses) {
            if (obj.equals(address)) {
                return obj;
            }
        }
        return null;
    }


}
