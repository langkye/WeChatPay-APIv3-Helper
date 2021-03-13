package com.langkye.api.paychl.wecahtpay.bean;

import lombok.Data;

@Data
public class BusinessInfo{

    private String	merchantShortname;
    private String	servicePhone;
    private SalesInfo	salesInfo;

}
