package com.langkye.api.paychl.wecahtpay.utils;

import com.langkye.api.paychl.wecahtpay.bean.ModifySettlement;
import com.langkye.api.paychl.wecahtpay.bean.Root;
import com.langkye.api.paychl.wecahtpay.common.ApplymentQueryType;
import com.langkye.api.paychl.wecahtpay.common.FileLocation;
/**
 * @author langkye
 */
public class WeChatPayUtil {
    WeChatPayWrapper weChatPayWrapper = new WeChatPayWrapper();
    /**
     * 特约商户进件: 提交申请单API
     *
     * @doc https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/tool/applyment4sub/chapter3_1.shtml
     */
    public String applyment(Root root) {
        return weChatPayWrapper.applyment(root);
    }

    /**
     * 特约商户进件: 查询申请状态
     *      通过【业务申请编号】或【申请单号】查询申请状态
     *
     *
     * @param arg    业务申请编号(business_code) | 申请单号(applyment_id)
     * @param aqType 查询方式 : BUSINESS_CODE | APPLYMENT_ID
     * @return 申请提交结果
     * @see ApplymentQueryType
     * @doc https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/tool/applyment4sub/chapter3_2.shtml
     */
    public String applymentStatus(String arg, ApplymentQueryType aqType){
        return weChatPayWrapper.applymentStatus(arg, aqType);
    }

    /**
     * 特约商户进件: 修改结算帐号API
     * @param modifySettlement 修改结算账号bean
     * @see ModifySettlement
     * @doc https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/tool/applyment4sub/chapter3_3.shtml
     */
    public String modifySettlement(ModifySettlement modifySettlement){
        return weChatPayWrapper.modifySettlement(modifySettlement);
    }

    /**
     * 特约商户进件: 查询结算账户API
     * @doc https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/tool/applyment4sub/chapter3_4.shtml
     */
    public String querySettlement(String subMchid){
        return weChatPayWrapper.querySettlement(subMchid);
    }

    /***
     * 上传图片
     *
     * @param location 图片位置： 本地｜远程
     * @param imageUrl 本地图片路径 或 网络图片url（大小2M以内）
     * @return example: {"media_id":"DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY"}
     */
    public String uploadPic(String imageUrl, FileLocation location) {
        return weChatPayWrapper.uploadPic(imageUrl,location);
    }
}
