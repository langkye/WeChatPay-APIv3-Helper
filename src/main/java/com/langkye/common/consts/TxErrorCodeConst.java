package com.langkye.common.consts;

/**
 * 交易常量
 *
 * @author langkye
 */
public enum TxErrorCodeConst {
    /**交易常量枚举*/
    TX_ERROR_900400("900400","参数错误"),
    TX_ERROR_900500("900500","系统异常错误"),
    TX_ERROR_900900("900900","调用支付通道异常"),
    ;

    /**错误码*/
    private final String code;

    private TxErrorCodeConst(String code, String message){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
