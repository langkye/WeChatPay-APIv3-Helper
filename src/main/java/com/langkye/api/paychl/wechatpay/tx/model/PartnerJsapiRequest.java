package com.langkye.api.paychl.wechatpay.tx.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 服务商 JSAPI 请求参数
 * 传参方式： body-对象传参（指该参数需在请求JSON传参），
 *          query-请求'?'传参(指该参数需在请求URL传参)，
 *          path-路径传参(指该参数为路径参数)
 * @author langkye
 */
@Data
public class PartnerJsapiRequest implements Serializable {

    /**
     * 传参方式：body
     * 描述：服务商申请的公众号appid。
     * 示例值：wx8888888888888888
     */
    @NotBlank(message = "服务商应用ID[spAppid]不能为空")
    @Length(min = 1, max = 32)
    private String spAppid;

    /**
     * 传参方式：body
     * 描述：服务商户号，由微信支付生成并下发
     * 示例值：1230000109
     */
    @NotBlank(message = "服务商户号[spMchid]不能为空")
    @Length(min = 1, max = 32)
    private String spMchid;

    /**
     * 传参方式：body
     * 描述：子商户申请的公众号appid。若sub_openid有传的情况下，sub_appid必填，且sub_appid需与sub_openid对应
     * 示例值：wxd678efh567hg6999
     */
    @Length(min = 1, max = 32)
    private String subAppid;

    /**
     * 传参方式：body
     * 描述：子商户的商户号，由微信支付生成并下发。
     * 示例值：1900000109
     */
    @NotBlank(message = "子商户应用ID[subMchid]不能为空")
    @Length(min = 1, max = 32)
    private String subMchid;

    /**
     * 传参方式：body
     * 描述：商品描述
     * 示例值：Image形象店-深圳腾大-QQ公仔
     */
    @NotBlank(message = "商品描述[description]不能为空")
    @Length(min = 1, max = 127)
    private String description;

    /**
     * 传参方式：body
     * 描述：商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一。
     * 示例值：1217752501201407033233368018
     */
    @NotBlank(message = "商户订单号[outTradeNo]不能为空")
    @Length(min = 6, max = 32)
    private String outTradeNo;

    /**
     * 传参方式：body
     * 描述：订单失效时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
     * 示例值：2018-06-08T10:34:56+08:00
     */
    @Length(min = 1, max = 64)
    @Pattern(regexp="[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}\\+[0-2][0-9]:[0-9]{2}", message = "订单失效时间[timeExpire]格式错误，示例值：2018-06-08T10:34:56+08:00")
    private String timeExpire;

    /**
     * 传参方式：body
     * 描述：附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用
     * 示例值：自定义数据
     */
    @Length(min = 1, max = 128)
    private String attach;

    /**
     * 传参方式：body
     * 描述：通知URL必须为直接可访问的URL，不允许携带查询串。
     * 示例值：https://www.weixin.qq.com/wxpay/pay.php
     */
    @NotBlank(message = "通知地址[notifyUrl]不能为空")
    @Length(min = 1, max = 256)
    private String notifyUrl;

    /**
     * 传参方式：body
     * 描述：订单优惠标记
     * 示例值：WXG
     */
    @Length(min = 1, max = 32)
    private String goodsTag;

    /**
     * 传参方式：body
     * 描述：结算信息
     * 参数类型：object
     */
    private SettleInfo settleInfo;

    /**
     * 传参方式：body
     * 描述：订单金额信息
     * 参数类型：object
     */
    @NotNull(message = "订单金额信息[amount]不能为空")
    private Amount amount;

    /**
     * 传参方式：body
     * 描述：支付者信息
     * 参数类型：object
     */
    @NotNull(message = "支付者信息[payer]不能为空")
    private Payer payer;

    /**
     * 传参方式：body
     * 描述：优惠功能
     * 参数类型：object
     */
    private Detail detail;

    /**
     * 传参方式：body
     * 描述：支付场景描述
     * 参数类型：object
     */
    @NotNull(message = "支付场景描述[sceneInfo]不能为空")
    private SceneInfo sceneInfo;
}
