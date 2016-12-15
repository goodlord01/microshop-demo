package com.microshop.web;

import com.microshop.customers.CustomerService;
import com.microshop.domain.*;
import com.microshop.exception.UnauthorizedException;
import com.microshop.orders.OrderService;
import com.microshop.seller.SellerRepository;
import com.microshop.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by min on 23/10/2016.
 */
@RequestMapping(value = "/orders")
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    CustomerService customerService;

    @Autowired
    SellerRepository sellerRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Order> getAllOrder(Pageable pageable,
                            HttpServletRequest request) {
        String openId = AuthUtils.getOpenId(request);
//        openId = "oRn0Ow0Z2KNhGcLSlAgNEht-YmOQ";
        if (StringUtils.hasText(openId))
            return orderService.getOrdersByCustomerId(openId, pageable);
        return new ArrayList<>();
    }

    @RequestMapping(value = "seller_list", method = RequestMethod.GET)
    public List<Order> getOrderBySeller(Pageable pageable, HttpServletRequest request) {
        String sellerId = AuthUtils.getOpenId(request);
        // String sellerId = "oSHnAv1oFo5Ekt6_g18O94eM8y7c";
        if (StringUtils.hasText(sellerId))
            return orderService.getOrdersBySellerId(sellerId, pageable);
        return new ArrayList<>();
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<OrderStatus, Integer> getAllStatus(HttpServletRequest request) {
        String openId = AuthUtils.getOpenId(request);
//        openId = "oRn0Ow0Z2KNhGcLSlAgNEht-YmOQ";
        return orderService.getStatusList(openId);
    }

    @RequestMapping(value = "/seller_status", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<OrderStatus, Integer> getAllSellerStatus(HttpServletRequest request) {
        String openId = AuthUtils.getOpenId(request);
//        openId = "oRn0Ow0Z2KNhGcLSlAgNEht-YmOQ";
        return orderService.getSellerStatusList(openId);
    }

    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public ResponseEntity<?> updateStatus(@RequestBody Order order) {
        String id = orderService.updateOrderStatus(order);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @RequestMapping(value = "/shipProduct", method = RequestMethod.POST)
    public ResponseEntity<?> shipProduct(@RequestBody Order theOrder,
                                         HttpServletRequest request) {
        String id = theOrder.getId();
        if (id != null) {
            Order order = orderService.getOrder(theOrder.getId());
            if (checkSellerOrderPemission(order, request)) {
                if (!order.getStatus().equals(OrderStatus.PAID)) {
                    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
                }
                order.setStatus(OrderStatus.DELIVERING);
                order.setTrackingNo(theOrder.getTrackingNo());
                orderService.updateOrder(order);
                return new ResponseEntity<>(theOrder.getId(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/receiveProduct/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> receiveProduct(@PathVariable(value = "id") String id, HttpServletRequest request) {
        Order order = orderService.getOrder(id);
        if (checkOrderPemission(order, request)) {
            if (!order.getStatus().equals(OrderStatus.DELIVERING)) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            order.setStatus(OrderStatus.COMPLETED);
            orderService.updateOrder(order);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@RequestBody OrderDTO orderDTO, HttpServletRequest request) throws Exception {
        String openId = AuthUtils.getOpenId(request);
        orderDTO.setOpenId(openId);
        if (orderDTO.getAddress() != null) {
            AddressDTO addr = orderDTO.getAddress();
            addr.setOpenId(openId);
        }
        Order savedOrder = orderService.createOrder(orderDTO);
        return new OrderResponse(savedOrder.getId(), openId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Order viewOrder(@PathVariable(value = "id") String id, HttpServletRequest request) {
        Order order = orderService.getOrder(id);
        if (checkOrderPemission(order, request)) {
            return order;
        }

        return null;
    }

    @RequestMapping(value = "/{id}/paid", method = RequestMethod.PATCH)
    public void paySuccess(@PathVariable("id") String id, HttpServletRequest request) {
        Order order = orderService.getOrder(id);
        if (checkOrderPemission(order, request)) {
            order.setStatus(OrderStatus.PAID);
            orderService.updateOrder(order);
        }
    }

    @RequestMapping(value = "/{id}/pay_failed", method = RequestMethod.PATCH)
    public void payFailed(@PathVariable("id") String id, HttpServletRequest request) {
        Order order = orderService.getOrder(id);
        if (checkOrderPemission(order, request)) {
            order.setStatus(OrderStatus.FAILED);
            orderService.updateOrder(order);
        }
    }


    private boolean checkOrderPemission(Order order, HttpServletRequest request) {
        String openId = AuthUtils.getOpenId(request);
        if (StringUtils.hasText(openId)) {
            if (order.getCustomer() != null && openId.equals(order.getCustomer().getOpenId()))
                return true;
            else
                throw new UnauthorizedException("No permission to view or edit the order");
        }
        return false;
    }

    private boolean checkSellerOrderPemission(Order order, HttpServletRequest request) {
        String openId = AuthUtils.getOpenId(request);
        if (StringUtils.hasText(openId)) {
            Seller seller = sellerRepository.findOne(openId);
            if (seller != null && openId.equals(order.getProduct().getSeller().getOpenId())) {
                return true;
            } else {
                throw new UnauthorizedException("No permission to view or edit the order");
            }

        }
        return false;
    }
}
