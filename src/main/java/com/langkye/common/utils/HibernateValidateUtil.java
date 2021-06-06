package com.langkye.common.utils;

import com.langkye.common.consts.TxErrorCodeConst;
import com.langkye.common.exception.TxException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.executable.ExecutableValidator;
import org.hibernate.validator.HibernateValidator;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * hibernate.validator工具类
 *
 * @author langkye
 */
public class HibernateValidateUtil {
    /**
     * 使用hibernate的注解来进行验证 failFast true:仅仅返回第一条错误信息 false返回所有错误，此处采用快速失败模式
     */
    private final static Validator validator = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();
    /**
     * 扩展的Validator
     */
    private final static ExecutableValidator executableValidator = validator.forExecutables();

    /**
     * 检验Java实体对象
     *
     * @param obj 校验的对象
     * @return String 错误提示信息
     */
    public static <T> String validateJavaBean(T obj) {
        Set<ConstraintViolation<T>> validateResult = validator.validate(obj);
        if (validateResult.isEmpty()) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();

        for (ConstraintViolation<T> v : validateResult) {
            sb.append("|").append(v.getMessage()).append("|");
        }

        return sb.toString();
    }

    /**
     * 检验Java实体对象
     *
     * @param obj 校验的对象
     * @throws TxException 交易异常
     */
    public static <T> void validateJavaBeanThrowsException(T obj) throws TxException {
        Set<ConstraintViolation<T>> validateResult = validator.validate(obj);
        if (validateResult.isEmpty()) {
            return;
        }

        final StringBuilder sb = new StringBuilder();

        for (ConstraintViolation<T> v : validateResult) {
            sb.append("|").append(v.getMessage()).append("|");
        }

        final TxException txException = new TxException();

        throw txException.setMessage(sb.toString()).setTxErrorCodeConst(TxErrorCodeConst.TX_ERROR_900400);
    }

    /**
     * 检验方法局部参数
     *
     * @param obj 校验的对象
     * @return String 错误提示信息
     */
    public static <T> String validateMethodParameter(T obj, Method method, Object[] args) {
        Set<ConstraintViolation<T>> validateResult = executableValidator.validateParameters(obj, method, args);
        if (validateResult.isEmpty()) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();

        for (ConstraintViolation<T> v : validateResult) {
            sb.append("|").append(v.getMessage()).append("|");
        }

        return sb.toString();
    }

    /**
     * 检验方法局部参数
     *
     * @param obj 校验的对象
     * @return String 错误提示信息
     */
    public static <T> void validateMethodParameterThrowsException(T obj, Method method, Object[] args) throws TxException {
        Set<ConstraintViolation<T>> validateResult = executableValidator.validateParameters(obj, method, args);
        if (validateResult.isEmpty()) {
            return;
        }

        final StringBuilder sb = new StringBuilder();

        for (ConstraintViolation<T> v : validateResult) {
            sb.append("|").append(v.getMessage()).append("|");
        }

        final TxException txException = new TxException();

        throw txException.setMessage(sb.toString()).setTxErrorCodeConst(TxErrorCodeConst.TX_ERROR_900400);
    }
}
