package com.langkye.api.paychl.wecahtpay.common;

/**
 * @author langkye
 */

public enum RequestMethod {
    GET("GET"),
    POST("POST")
    ;

    private String code;

    RequestMethod(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
