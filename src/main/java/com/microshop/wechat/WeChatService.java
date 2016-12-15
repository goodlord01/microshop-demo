package com.microshop.wechat;

import com.microshop.domain.*;
import com.microshop.utils.HashUtils;
import com.microshop.utils.ObjectUtils;
import com.microshop.utils.RandomUtils;
import com.microshop.utils.XmlUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by yan on 11/9/2016.
 */
@Service
public class WeChatService {

    private Log logger = LogFactory.getLog(WeChatService.class);

    @Value("${yunsoo.client.third_api}")
    private String thirdApiUrl;

    @Value("${yunsoo.wechat.app_id}")
    private String appId;

    @Value("${yunsoo.wechat.app_secret}")
    private String appSecret;

    @Value("${yunsoo.wechat.private_key}")
    private String privateKey;

    @Value("${yunsoo.wechat.mch_id}")
    private String mchId;

    @Value("${yunsoo.wechat.base_url}")
    private String baseUrl;

    private RestTemplate dataApiTemplate, wechatTemplate;

    public WeChatService() {
        dataApiTemplate = new RestTemplate();
        wechatTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> listMessageConvert = wechatTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : listMessageConvert) {
            if(httpMessageConverter instanceof StringHttpMessageConverter){
                 MediaType type = new MediaType("text", "plain", Charset.forName("UTF-8"));
                ((StringHttpMessageConverter) httpMessageConverter).setSupportedMediaTypes(Arrays.asList( type, MediaType.ALL));
                break;
            }
        }
    }




    public WeChatWebAccessToken getWebAccessToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + appId +
                "&secret=" + appSecret +
                "&code=" + code +
                "&grant_type=authorization_code";

        String webToken = wechatTemplate.getForObject(url, String.class);
        try {
            webToken = new String(webToken.getBytes("ISO-8859-1"), "UTF-8");
            WeChatWebAccessToken token = ObjectUtils.toObject(webToken, WeChatWebAccessToken.class);
            if(token.getErrorCode() > 0){
                logger.error("get wechat access token error: " + token.getErrorMsg());
                return null;
            }

            return token;
        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }

        return null;
    }

    public WeChatUser getUserInfo(String accessToken, String openId) {
        String user = wechatTemplate.getForObject("https://api.weixin.qq.com/sns/userinfo?access_token={accessToken}&openid={openId}&lang=zh_CN",
                String.class, accessToken, openId);
        try {
            user = new String(user.getBytes("ISO-8859-1"), "UTF-8");
            return ObjectUtils.toObject(user, WeChatUser.class);
        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }

        return null;
    }

    public WeChatAccessToken getAccessToken() {
        String url = thirdApiUrl + "/wechat/token";
        return dataApiTemplate.getForObject(url, WeChatAccessToken.class);
    }

    public WeChatConfig getWechatConfig(String url, String nonceStr){
        WeChatConfig config = new WeChatConfig();
        config.setAppId(appId);
        if(!StringUtils.hasText(nonceStr))
            config.setNoncestr(RandomUtils.generateString(30));
        else
            config.setNoncestr(nonceStr);
        config.setTimestamp(new DateTime().getMillis());
        WeChatAccessToken token = getAccessToken();
        String jsapi_ticket = token.getJsapiTicket();
        String param = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + config.getNoncestr() + "&timestamp=" + config.getTimestamp() + "&url=" + url;
        config.setSignature(HashUtils.sha1HexString(param));
        return config;
    }

    public WeChatData getWeChatPayConfig(String prePayId, long timeStamp, String nonceStr) throws UnsupportedEncodingException {
        WeChatData data = new WeChatData();
        data.setValue("appId", appId);
        data.setValue("timeStamp", timeStamp);
        data.setValue("nonceStr", nonceStr);
        data.setValue("package", "prepay_id=" + prePayId);
        data.setValue("signType", "MD5");
        data.addSign(privateKey);
        return data;
    }


    public WeChatOrderResult saveWeChatUnifiedOrder(String openId, String ip, String nonceString, Order order) {
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

        BigDecimal totalFee = order.getPrice().multiply(new BigDecimal(100));  //以分为单位
        Product product = order.getProduct();
        StringBuilder detailJson = new StringBuilder("{\"goods_detail\": [{\"goods_id\":\"");
        detailJson.append(product.getId());
        detailJson.append("\",\"goods_name\":\"");
        detailJson.append(product.getName());
        detailJson.append("\",\"quantity\":");
        detailJson.append(order.getQuantity());
        detailJson.append(",\"price\":");
        detailJson.append(product.getPrice());
        detailJson.append("}]}");

        WeChatData data = new WeChatData();
        data.setValue("appid", appId);
        data.setValue("mch_id", mchId);
        data.setValue("nonce_str", nonceString);
        data.setValue("body", product.getName());
        data.setValue("out_trade_no", order.getId().toString());
        data.setValue("total_fee", totalFee.intValue());
        data.setValue("detail", detailJson.toString());
        data.setValue("spbill_create_ip", ip);
        data.setValue("trade_type", "JSAPI");
        data.setValue("notify_url", baseUrl + "notify");
        data.setValue("openid", openId);

        try {
            data.addSign(privateKey);
            String result = wechatTemplate.postForObject(url, data.toXml(), String.class);
            result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
            WeChatOrderResult orderResult = XmlUtils.convertXmlToObject(result, WeChatOrderResult.class);
            if("SUCCESS".equals(orderResult.getReturnCode()) &&  "SUCCESS".equals(orderResult.getResultCode()))
                return orderResult;
            else{
                if(!"SUCCESS".equals(orderResult.getReturnCode()))
                    logger.error("get wechat unified pay error: " + orderResult.getReturnMsg());
                else
                    logger.error("get wechat unified pay error: code: " + orderResult.getErrCode() + ", message: " + orderResult.getErrCodeDes());
            }
        } catch (JAXBException e) {
            logger.error(e);
        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        }

        return null;
    }

}
