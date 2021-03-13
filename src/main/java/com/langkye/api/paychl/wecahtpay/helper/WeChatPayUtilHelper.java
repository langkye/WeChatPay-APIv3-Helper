package com.langkye.api.paychl.wecahtpay.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.langkye.api.paychl.wecahtpay.bean.*;
import com.langkye.api.paychl.wecahtpay.common.ApplymentQueryType;
import com.langkye.api.paychl.wecahtpay.common.FileLocation;
import com.langkye.api.paychl.wecahtpay.utils.WeChatPayUtil;
import com.langkye.api.paychl.wecahtpay.utils.WeChatPayWrapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author langkye
 */
public class WeChatPayUtilHelper {

    public static void main(String[] args) throws Exception {
        //PrivateKey privateKey = WeChatPayWrapper.getPrivateKey(WeChatPayWrapper.MCH_PRI_KEY_PATH);
        //System.out.println("privateKey = " + privateKey);

        //特约商户进件
        //applymentHelper();

        //查询申请状态
        //queryStatusByApplymentIdHelper();
        queryStatusByBusinessCodeHelper();

        //图片上传
        //uploadPicHelper();

        //加密 ｜ 解密
        //rsaEncytpAndDecrypt();
    }


    /***
     * 接口示例：APPLYMENT_20210312093332
     *      - 特约商户进件: 提交申请单API
     * 返回示例:
     *      - {"applyment_id":2000002177371579}
     *      - {"applyment_id":2000002177345181}
     */
    public static void applymentHelper() throws Exception {
        Root root = BeanHelper.toConvert();
        System.out.println("root = " + root);
        String applyment = WeChatPayUtil.applyment(root);
        System.out.println("applyment = " + applyment);
    }


    /***
     * 接口示例：
     *      - 查询申请状态:通过【申请单号】查询申请状态
     *      返回示例:
     *          {"applyment_id":2000002177293742,"applyment_state":"APPLYMENT_STATE_REJECTED","applyment_state_msg":"","audit_detail":[{"reject_reason":"填写的商户名称与营业执照注册号不匹配，查询营业执照名称应为：国际铜专业协会北京代表处，请核实填写内容与查询名称是否一致（若一致仍无法提交请联系微信支付客服处理）"}],"business_code":"APPLYMENT_20210312031540","sign_url":"https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQHk7zwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyRnZTcjlrb3JlUjIxcmNBTzF3Y3MAAgTMF0tgAwQAjScA"}
     */
    public static void queryStatusByApplymentIdHelper() throws Exception {
        String applymentStatus = WeChatPayUtil.applymentStatus("2000002177345181", ApplymentQueryType.APPLYMENT_ID);
        System.out.println("applymentStatus = " + applymentStatus);
    }

    /***
     * 接口示例：
     *      - 查询申请状态:通过【业务申请编号】查询申请状态
     *      返回示例：
     *          applymentStatus = {"code":"PARAM_ERROR","detail":{"location":"uri_template","value":"APPLYMENT_20210312084316"},"message":"无法将输入源“/uri_template/applyment_id”映射到目标字段“申请单号 ”中，此字段需要一个合法的 64 位无符号整数"}
     */
    public static void queryStatusByBusinessCodeHelper() throws Exception {
        String applymentStatus = WeChatPayUtil.applymentStatus("APPLYMENT_20210312093332", ApplymentQueryType.BUSINESS_CODE);
        System.out.println("applymentStatus = " + applymentStatus);
    }


    /**
     * 接口示例：
     *      - 上传图片
     *      返回示例：
     *          {"media_id":"ysJR93_VqntdiTwOPM2fPBKrawp6xg__rWmGoQ5nyZG4E3-Uz9sbhihjzUdKn5N3eAF_nYt29TXCj6Xjgc9otSkxZhHf5NDQNO_PNgkLH4c"}
     *          {"media_id":"ysJR93_VqntdiTwOPM2fPK4yBkMaktixy63YmljUvdWhBYEjtcUVEvv8yyOiKrtzDCFfC4Y7Z9r5G91s7_SmGv6S_bZtcE7NbhRm-eEryVY"}
     */
    public static void uploadPicHelper() throws IOException, URISyntaxException {
        //本地文件
        String filePath1 = "/Users/langkye/Pictures/1.png";
        String media1 = WeChatPayUtil.uploadPic(filePath1, FileLocation.LOCALHOST);
        System.out.println("media1 = " + media1);

        //远程文件
        String filePath2 = "http://mocard-1251489075.cos.ap-beijing.myqcloud.com/etc/user/zx/advertising/b8f3cd0387fd6f278e9e4bcf823c71f.png";
        String media2= WeChatPayUtil.uploadPic(filePath2, FileLocation.REMOTE);
        System.out.println("media2 = " + media2);

    }

    /**
     * 加密消息 | 解密
     *
     * @throws IOException
     */
    public static void rsaEncytpAndDecrypt() throws IOException, IllegalBlockSizeException, BadPaddingException {
        //X509Certificate certificate = WeChatPayWrapper.getCertificate(WeChatPayWrapper.WECHATPAY_PUB_KEY_PATH);
        //Cipher cipher = WeChatPayWrapper.getCipher(certificate);
        //String s = WeChatPayWrapper.rsaEncryptOAEP("xx", cipher);
        //System.out.println(s);

        String source = "{\"code\":200,\"message\":\"success\"}";
        System.out.println("source = " + source);

        //String encrypt = WeChatPayWrapper.rsaEncryptOAEP(source,WeChatPayWrapper.getCertificate(WeChatPayWrapper.MCH_PUB_KEY_PATH));
        String encrypt = WeChatPayWrapper.rsaEncryptOAEP(source);
        System.out.println("encrypt = " + encrypt);

        String target = WeChatPayWrapper.rsaDecryptOAEP(encrypt);
        System.out.println("target = " + target);

        System.out.println("验证结果：" + source.equals(target));
    }

}