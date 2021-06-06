package com.langkye.api.paychl.wechatpay;

import com.langkye.api.paychl.wechatpay.tx.model.PartnerJsapiRequest;
import com.langkye.common.exception.TxException;
import com.langkye.common.utils.HibernateValidateUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class ValidatorTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testValidator(){
        final PartnerJsapiRequest jsapiRequest = new PartnerJsapiRequest();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<PartnerJsapiRequest>> constraintViolations = validator.validate(jsapiRequest);
        for (ConstraintViolation<PartnerJsapiRequest> constraintViolation : constraintViolations) logger.error("错误：{}" , constraintViolation.getMessage());
    }

    @Test
    public void testValidatorNoException(){
        final PartnerJsapiRequest jsapiRequest = new PartnerJsapiRequest();
        final String s = HibernateValidateUtil.validateJavaBean(jsapiRequest);
        if (StringUtils.isNotBlank(s)){
            logger.error("异常信息：{}",s);
        }
    }

    @Test
    public void testValidatorException() throws TxException {
        final PartnerJsapiRequest jsapiRequest = new PartnerJsapiRequest();
        HibernateValidateUtil.validateJavaBeanThrowsException(jsapiRequest);
    }
}
