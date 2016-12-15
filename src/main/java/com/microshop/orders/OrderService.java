package com.microshop.orders;

import com.microshop.address.AddressService;
import com.microshop.customers.CustomerService;
import com.microshop.domain.*;
import com.microshop.products.ProductService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by min on 23/10/2016.
 */

@Service
@Transactional
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    CustomerService custService;

    @Autowired
    AddressService addrService;

    @Autowired
    ProductService productService;


    public Order createOrder(OrderDTO orderDTO)
    {
        Order order = convertToOrder(orderDTO);
        return orderRepo.save(order);
    }

    public Order getOrder(String id)
    {
        return orderRepo.findOne(id);
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepo.findAll(pageable);
    }

    public List<Order> getOrdersByCustomerId(String openId, Pageable pageable) {
        Page<Order> page = orderRepo.findByCustIdOrderByCreatedOnDesc(openId, pageable);
        List<Order> orders = page.getContent();
        return filterOrderData(orders);
    }

    public List<Order> getOrdersBySellerId(String sellerId, Pageable pageable){
        Page<Order> orderPage = orderRepo.findOrderListBySellerId(sellerId, pageable);
        return filterOrderData(orderPage.getContent());
    }

    public void updateOrder(Order order) {
        orderRepo.save(order);
    }

    public String updateOrderStatus(Order order) {
        Order so = getOrder(order.getId());
        so.setStatus(order.getStatus());
        so.setTrackingNo(order.getTrackingNo());
        orderRepo.save(so);
        return so.getId();
    }

    public void updateOrderCustomer(String id,  Customer customer){
        Order order = getOrder(id);
        if(order.getCustomer() == null) {
            order.setCustomer(customer);
            Address address = order.getAddress();
            if(StringUtils.hasText(address.getOpenId()))
                address.setOpenId(customer.getOpenId());

            orderRepo.save(order);
        }
    }

    private Order convertToOrder(OrderDTO orderDTO) {

        Product product = productService.getByProductId(orderDTO.getProductId());
        AddressDTO addressDTO = orderDTO.getAddress();

        Order order = new Order();

        Address address = null;

       if(StringUtils.hasText(orderDTO.getOpenId())) {
           Customer customer = custService.getByOpenId(orderDTO.getOpenId());
           order.setCustomer(customer);
       }

        if(addressDTO != null && addressDTO.getOpenId() != null) {
            address = addrService.isAddressExist(addressDTO.toAddress());
        }

        if (address == null) {
            address = addrService.createAddress(addressDTO);
        }
        order.setAddress(address);

        order.setProduct(product);

        order.setQuantity(orderDTO.getQuantity());
        order.setStatus(OrderStatus.NEW);

        BigDecimal totalPrice = product.getPrice().multiply(new BigDecimal(orderDTO.getQuantity()));
        order.setPrice(totalPrice);
        order.setCreatedOn(new DateTime());
        return order;
    }

    private HashMap<OrderStatus, Integer> countStatus(List<OrderStatus> statusList) {
        HashMap<OrderStatus, Integer> map = new HashMap<>();
        Integer paid = 0;
        Integer delivering = 0;
        for(OrderStatus item : statusList) {
            if(item==OrderStatus.PAID) {
                paid++;
            } else if(item==OrderStatus.DELIVERING) {
                delivering++;
            }
        }
        map.put(OrderStatus.PAID,paid);
        map.put(OrderStatus.DELIVERING,delivering);
        return map;
    }

    public Map<OrderStatus, Integer> getStatusList(String openId) {
        List<OrderStatus> statusList = orderRepo.getAllStatusByCustId(openId);
        return countStatus(statusList);
    }

    public Map<OrderStatus, Integer> getSellerStatusList(String openId) {
        List<OrderStatus> statusList = orderRepo.getAllStatusBySellerId(openId);
        return countStatus(statusList);
    }

    private List<Order> filterOrderData(List<Order> orders) {
        List<Order> convOrders = new ArrayList<>();
        for (Order order: orders) {
            convOrders.add(simplifyOrder(order));
        }
        return convOrders;
    }

    private Order simplifyOrder(Order order){
        Order convOrder = new Order();
        Address address = order.getAddress();
        Address convAddr = new Address();

        String addressLine = address.getProvince() + '省' + address.getCity() + '市' + address.getAddress();
        convAddr.setAddress(addressLine);
        convAddr.setPhone(address.getPhone());
        convAddr.setName(address.getName());

        convOrder.setAddress(convAddr);

        Product product = order.getProduct();
        Product convProduct = new Product();
        convProduct.setName(product.getName());
        convProduct.setId(product.getId());
        convProduct.setPrice(product.getPrice());

        convOrder.setProduct(convProduct);
        convOrder.setCreatedOn(order.getCreatedOn());
        convOrder.setId(order.getId());
        convOrder.setQuantity(order.getQuantity());
        convOrder.setPrice(order.getPrice());
        convOrder.setStatus(order.getStatus());

        return convOrder;
    }


}
