package com.langkye.api.paychl.wecahtpay.utils;

import com.langkye.api.paychl.wecahtpay.bean.Root;
import com.langkye.api.paychl.wecahtpay.common.ApplymentQueryType;
import com.langkye.api.paychl.wecahtpay.common.FileLocation;

import java.io.IOException;
import java.net.URISyntaxException;


/**
 * @author langkye
 */
public class WeChatPayUtil {
    /**
     * 特约商户进件: 提交申请单API
     *
     * @doc https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/tool/applyment4sub/chapter3_1.shtml
     */
    public static String applyment(Root root) throws Exception {
        return WeChatPayWrapper.applyment(root);
    }

    /**
     * 查询申请状态:通过业务申请编号查询申请状态
     *
     * @param arg    业务申请编号(business_code) | 申请单号(applyment_id)
     * @param aqType 查询方式 : BUSINESS_CODE | APPLYMENT_ID
     * @return
     * @throws Exception
     * @see ApplymentQueryType
     */
    public static String applymentStatus(String arg, ApplymentQueryType aqType) throws Exception {
        return WeChatPayWrapper.applymentStatus(arg, aqType);
    }

    /***
     * 上传图片
     *
     * @param location 图片位置： 本地｜远程
     * @param imageUrl 本地图片路径 或 网络图片url（大小2M以内）
     * @return example: {"media_id":"DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY"}
     */
    public static String uploadPic(String imageUrl, FileLocation location) throws IOException, URISyntaxException {
        return WeChatPayWrapper.uploadPic(imageUrl,location);
    }
}
