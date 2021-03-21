package com.langkye.api.paychl.wecahtpay.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author langkye
 */

@AllArgsConstructor
@Getter
public enum RequestMethod {
    GET("GET"),
    POST("POST")
    ;

    private String code;
}
