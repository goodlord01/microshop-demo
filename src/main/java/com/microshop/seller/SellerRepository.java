package com.microshop.seller;

import com.microshop.domain.Seller;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yan on 11/25/2016.
 */
public interface SellerRepository extends CrudRepository<Seller, String> {

}
