package com.langkye.api.paychl.wechatpay.tx.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 订单金额[PartnerJsapiRequest下单子对象]
 *
 * @author langkye
 * @date 2021/6/6
 */
@Data
public class Amount implements Serializable {
    /**
     * 描述：订单总金额，单位为分。
     * 示例值：100
     */
    @NotNull(message = "订单金额[total]不能为空。")
    private Integer total;

    /**
     * 描述：CNY：人民币，境内商户号仅支持人民币。
     * 示例值：CNY
     */
    @NotNull(message = "货币类型[currency]不能为空。")
    @Length(min = 1, max = 16)
    private String currency;
}
