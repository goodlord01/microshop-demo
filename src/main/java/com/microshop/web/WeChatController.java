package com.microshop.web;

import com.microshop.customers.CustomerService;
import com.microshop.domain.*;
import com.microshop.exception.UnauthorizedException;
import com.microshop.orders.OrderService;
import com.microshop.thumbs.ThumbsService;
import com.microshop.utils.AuthUtils;
import com.microshop.utils.IpUtils;
import com.microshop.wechat.WeChatService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

/**
 * Created by min on 04/11/2016.
 */

@RequestMapping(value="/wechat")
@RestController
public class WeChatController {

    private Log logger = LogFactory.getLog(WeChatController.class);

    @Autowired
    CustomerService customerService;

    @Autowired
    WeChatService wechatService;

    @Autowired
    OrderService orderService;

    @Autowired
    ThumbsService thumbsService;

    @Value("${yunsoo.encode.salt}")
    private String hashSalt;

    private static final String USER_STATE="user";

    //return openid
    @RequestMapping(value = "handlecode" , method = RequestMethod.GET)
    public void handleCode(@RequestParam(value = "code", required = false) String code,
                           @RequestParam(value = "state", required = false) String state,
                           @RequestParam(value = "nonce_str", required = false) String nonceString,
                           @RequestParam(value = "order_id", required = false) String orderId,
                           @RequestParam(value = "seller_openid",required = false) String sellerOpenId,
                           @RequestParam(value = "return_url") String redirectUrl,
                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!StringUtils.hasText(code))
            throw new Exception("no wechat code in the request, url: " + request.getQueryString());

        WeChatWebAccessToken accessToken = wechatService.getWebAccessToken(code);
        if(accessToken == null)
            throw new Exception("get wechat access token error");

        Customer currentCustomer;
        if(USER_STATE.equals(state)){
            WeChatUser user = wechatService.getUserInfo(accessToken.getAccessToken(), accessToken.getOpenId());
            Customer existCustomer = customerService.getByOpenId(user.getOpenId());
            if(existCustomer == null) {
                Customer customer = new Customer();
                customer.setOpenId(user.getOpenId());
                customer.setNickname(user.getNickName());
                customer.setCity(user.getCity());
                customer.setProvince(user.getProvince());
                customer.setGravatarUrl(user.getImageUrl());
                customer.setSex(Boolean.valueOf(user.getSex()));
                currentCustomer = customerService.createCustomer(customer);
            }
            else{
                currentCustomer = existCustomer;
                currentCustomer.setNickname(user.getNickName());
                currentCustomer.setCity(user.getCity());
                currentCustomer.setProvince(user.getProvince());
                currentCustomer.setGravatarUrl(user.getImageUrl());
                currentCustomer.setSex(Boolean.valueOf(user.getSex()));
                customerService.updateCustomer(currentCustomer);
            }
        }
        else {
            Customer existCustomer = customerService.getByOpenId(accessToken.getOpenId());
            if(existCustomer == null) {
                Customer customer = new Customer();
                customer.setOpenId(accessToken.getOpenId());
                currentCustomer = customerService.createCustomer(customer);
            }
            else
                currentCustomer = existCustomer;
        }

        if(StringUtils.hasText(orderId)){
            orderService.updateOrderCustomer(orderId, currentCustomer);
            String orderResult = getUnifiedOrder(orderId, accessToken.getOpenId(),nonceString, request);
            redirectUrl += "?id=" + orderResult + "&order_id=" + orderId;
        }

        if(StringUtils.hasText(sellerOpenId)){
            ThumbsDTO thumbs = new ThumbsDTO();
            thumbs.setOpenid(accessToken.getOpenId());
            thumbs.setSellersOpenid(sellerOpenId);
            thumbs.setCreatedDatetime(new Date());
            thumbsService.createThumbs(thumbs);
        }

        AuthUtils.setOpenId(response, accessToken.getOpenId(), hashSalt);

        response.sendRedirect(redirectUrl);
    }

    @RequestMapping(value = "config", method = RequestMethod.GET)
    public WeChatConfig getWeChatConfig(@RequestParam("url") String url,
                                        @RequestParam(value = "nonce_str",required = false) String nonceString){
        return wechatService.getWechatConfig(url, nonceString);
    }

    @RequestMapping(value = "pay/config", method = RequestMethod.GET)
    public Map<String, Object> getWeChatPayConfig(@RequestParam("pre_pay_id") String id,
                                                  @RequestParam("nonce_str") String nonceString,
                                                  @RequestParam("timestamp") long timestamp) throws UnsupportedEncodingException {
        return wechatService.getWeChatPayConfig(id, timestamp, nonceString).getValues();
    }

    @RequestMapping(value = "pay/{order_id}", method = RequestMethod.POST)
    public Map<String, Object> getWeChatPayConfigWithOrderId(@PathVariable(value = "order_id") String orderId,
                                                  @RequestParam("nonce_str") String nonceString,
                                                  @RequestParam("timestamp") long timestamp, HttpServletRequest request) throws Exception {

        String  openId = AuthUtils.getOpenId(request);
        String prePayId = getUnifiedOrder(orderId, openId, nonceString, request);
        return wechatService.getWeChatPayConfig(prePayId, timestamp, nonceString).getValues();
    }

    @RequestMapping(value = "notify", method = RequestMethod.GET)
    public void NotifyWechat(){

    }

    private String getUnifiedOrder(@RequestParam("order_id") String id,
                                             @RequestParam(value = "openid") String openId,
                                             @RequestParam("nonce_str") String nonceString,
                                             HttpServletRequest request){
        Order order = orderService.getOrder(id);
        if(!StringUtils.hasText(openId) || order.getCustomer() == null || !openId.equals(order.getCustomer().getOpenId()))
            throw new UnauthorizedException("don't have permission to create order with id: " + id);

        String ip = IpUtils.getIpFromRequest(request);
        WeChatOrderResult result = wechatService.saveWeChatUnifiedOrder(openId, ip,nonceString, order);

        return result.getPrepayId();

    }
}
