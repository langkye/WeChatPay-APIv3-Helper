package com.langkye.api.paychl.wechatpay.tx.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 场景信息[PartnerJsapiRequest下单子对象]
 *
 * @author langkye
 * @date 2021/6/6
 */
@Data
public class SceneInfo implements Serializable {
    /**
     * 用户终端IP
     * 描述：用户的客户端IP，支持IPv4和IPv6两种格式的IP地址。
     * 示例值：14.23.150.211
     */
    @Length(min = 1, max = 45)
    @NotBlank(message = "用户终端IP[payerClientIp]不能为空。")
    private String payerClientIp;

    /**
     * 商户端设备号
     * 描述：商户端设备号（门店号或收银设备ID）。
     * 示例值：013467007045764
     */
    @Length(min = 1, max = 32)
    @NotBlank(message = "商户端设备号[deviceId]不能为空。")
    private String deviceId;
}
