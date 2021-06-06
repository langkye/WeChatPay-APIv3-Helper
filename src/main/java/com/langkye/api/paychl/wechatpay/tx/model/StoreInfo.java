package com.langkye.api.paychl.wechatpay.tx.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 商户门店信息场景信息[PartnerJsapiRequest.SceneInfo下单子对象]
 *
 * @author langkye
 * @date 2021/6/6
 */
@Data
public class StoreInfo implements Serializable {
    /**
     * 门店编号
     * 描述：商户侧门店编号
     * 示例值：0001
     */
    @Length(min = 1, max = 32)
    @NotBlank(message = "门店编号[id]不能为空。")
    private String id;

    /**
     * 门店名称
     * 描述：商户侧门店名称
     * 示例值：腾讯大厦分店
     */
    @Length(min = 1, max = 32)
    private String name;

    /**
     * 地区编码
     * 描述：地区编码，详细请见省市区编号对照表。
     * 示例值：440305
     */
    @Length(min = 1, max = 32)
    private String areaCode;

    /**
     * 详细地址
     * 描述：详细的商户门店地址
     * 示例值：广东省深圳市南山区科技中一道10000号
     */
    @Length(min = 1, max = 512)
    private String address;
}
