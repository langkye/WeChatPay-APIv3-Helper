package com.langkye.api.paychl.wecahtpay.bean;

import lombok.Data;

@Data
public class Root{
    private String	businessCode;
    private ContactInfo	contactInfo;
    private SubjectInfo	subjectInfo;
    private BusinessInfo	businessInfo;
    private SettlementInfo	settlementInfo;
    private BankAccountInfo	bankAccountInfo;
    private AdditionInfo	additionInfo;
}
