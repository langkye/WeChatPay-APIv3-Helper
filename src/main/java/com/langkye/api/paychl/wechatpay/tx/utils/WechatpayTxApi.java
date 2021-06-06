package com.langkye.api.paychl.wechatpay.tx.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.langkye.api.paychl.wechatpay.common.WeChatPayUtil;
import com.langkye.api.paychl.wechatpay.tx.model.PartnerJsapiRequest;
import com.langkye.api.paychl.wechatpay.common.WechatPayApiConst;
import com.langkye.common.exception.TxException;
import com.langkye.common.utils.HibernateValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import javax.validation.*;

/**
 * 微信支付-交易工具类
 *
 * @doc https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_1.shtml
 * @author langkye
 */
public class WechatpayTxApi {
    private static final Logger logger = LoggerFactory.getLogger(WechatpayTxApi.class);

    WeChatPayUtil wcpw = new WeChatPayUtil();

    /**
     * 服务商JSAPI下单
     *
     * @param request PartnerJsapiRequest 服务商jsapi请求参数对象
     * @return Object
     *         正常示例：{"prepay_id": "wx2611215250487459928b659bd466620000"}
     *         错误示例：{"PARAM_ERROR": "xxx", ...}
     */
    public Object jsapiForPartner(PartnerJsapiRequest request) throws TxException {
        //校验参数
        HibernateValidateUtil.validateJavaBeanThrowsException(request);
        //驼峰转下划线
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        String requestParams = JSON.toJSONString(request, config);
        //发送请求
        final String response;
        try {
            response = wcpw.sendPost(requestParams, WechatPayApiConst.WECHAT_PAY_PARTNER_TX_JSAPI.getUrl());
        } catch (Exception e) {
            logger.error("调用微信支付通道异常：{}", e.getMessage());
            final TxException te = new TxException();
            te.setMessage(e.getMessage());
            throw te;
        }
        return response;
    }

}
