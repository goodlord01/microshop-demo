package com.microshop.orders;

import com.microshop.domain.Order;
import com.microshop.domain.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by min on 23/10/2016.
 */
public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("from Order o where o.customer.openId = :openId and o.status != 'FAILED' order by o.createdOn desc ")
    Page<Order> findByCustIdOrderByCreatedOnDesc(@Param("openId")String openId, Pageable pageable);

    @Query("select o.status from Order o where o.customer.openId = :openId and o.status != 'FAILED'")
    List<OrderStatus> getAllStatusByCustId(@Param("openId") String openId);

    @Query("from Order o where o.product.seller.openId = :sellerId and o.status != 'FAILED' order by o.createdOn desc")
    Page<Order> findOrderListBySellerId(@Param("sellerId") String sellerId, Pageable pageable);

    @Query("select o.status from Order o where o.product.seller.openId = :sellerId and o.status != 'FAILED'")
    List<OrderStatus> getAllStatusBySellerId(@Param("sellerId") String sellerId);
}
