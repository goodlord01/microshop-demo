package com.microshop.products;

import com.microshop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Tuple;
import java.util.List;

/**
 * Created by min on 18/10/2016.
 */

//@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);

    List<Product> findByNameContaining(@Param("word") String word);

    @Query("SELECT p.id, p.price FROM Product p where p.seller.id = :sellerOpenId order by p.id asc")
    List<Tuple> findAllProductBySellerId(@Param("sellerOpenId") String sellerId);

}


