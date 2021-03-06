package com.langkye.api.paychl.wechatpay.common;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import okhttp3.HttpUrl;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

/**
 * @author langkye
 * @doc https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/pages/applyment4sub.shtml
 */
public class WeChatPayUtil {
    private static final Logger logger = LoggerFactory.getLogger(WeChatPayUtil.class);

    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36";
    public static String WECHATPAY_PUB_KEY_PATH;
    public static String MCH_PUB_KEY_PATH;
    public static String MCH_PRI_KEY_PATH;
    public static String MCH_ID = "1606010980";
    public static String API_V3KEY = "etrqerqe32424312HHH3452345dsf432";

    static {
        Properties properties = new Properties();
        try {
            InputStream inputStream = (new ClassPathResource("wechatpay-config.properties")).getInputStream();
            properties.load(inputStream);
            MCH_PUB_KEY_PATH = properties.getProperty("wechat.pay.mch.public");
            WECHATPAY_PUB_KEY_PATH = properties.getProperty("wechat.pay.pem");
            MCH_PRI_KEY_PATH = properties.getProperty("wechat.pay.mch.private");
            API_V3KEY = properties.getProperty("wechat.pay.mch.apiV3Key");
            MCH_ID = properties.getProperty("wecaht.pay.mch.mchId");
        } catch (IOException e) {
            System.out.println("??????????????????[wechatpay-config.properties]??????!");
        }
    }

//-----------------------------------------------------------??????-----------------------------------------------------------//
    /**
     * ?????????????????????
     *
     * @param certPath ??????????????????????????? ???????????????????????? apiclient_cert
     * @return
     * @throws IOException
     */
    public static String getSerialNo(String certPath) {
        X509Certificate certificate = getCertificate(certPath);
        return certificate.getSerialNumber().toString(16).toUpperCase();
    }

    /**
     * ???????????????
     *
     * @param filename ??????????????????  (required)
     * @return X509??????
     */
    public static X509Certificate getCertificate(String filename) {
        ClassPathResource resource = new ClassPathResource(filename);
        try (InputStream inputStream = resource.getInputStream()) {
            try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
                CertificateFactory cf = CertificateFactory.getInstance("X509");
                X509Certificate cert = (X509Certificate) cf.generateCertificate(bis);
                cert.checkValidity();
                return cert;
            } catch (CertificateExpiredException e) {
                throw new RuntimeException("???????????????", e);
            } catch (CertificateNotYetValidException e) {
                throw new RuntimeException("??????????????????", e);
            } catch (CertificateException e) {
                throw new RuntimeException("?????????????????????", e);
            }
        } catch (IOException e) {
            throw new RuntimeException("??????????????????", e);
        }
    }

    /**
     * ??????token
     *
     * @param method       ????????????         (????????????GET???POST url(??????url)
     * @param url          ????????????         (????????????)
     * @param body         ????????????         (??????body???GET?????????body???""???POST?????????body??????????????????json???)
     * @param merchantId   ?????????           (?????????)
     * @param certSerialNo API???????????????     (???????????????)
     * @return ?????????
     * @throws Exception ??????
     */
    public static String getToken(String method, String url, String body, String merchantId, String certSerialNo) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String signStr = "";
        HttpUrl httpurl = HttpUrl.parse(url);
        String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
        long timestamp = System.currentTimeMillis() / 1000;
        if (StringUtils.isEmpty(body)) {
            body = "";
        }
        assert httpurl != null;
        String message = buildMessage(method, httpurl, timestamp, nonceStr, body);
        String signature = sign(message.getBytes(StandardCharsets.UTF_8), MCH_PRI_KEY_PATH);
        signStr = "mchid=\"" + merchantId
                + "\",nonce_str=\"" + nonceStr
                + "\",timestamp=\"" + timestamp
                + "\",serial_no=\"" + certSerialNo
                + "\",signature=\"" + signature + "\"";
        return signStr;
    }

    public static String buildMessage(String method, HttpUrl url, long timestamp, String nonceStr, String body) {
        String canonicalUrl = url.encodedPath();
        if (url.encodedQuery() != null) {
            canonicalUrl += "?" + url.encodedQuery();
        }
        return method + "\n"
                + canonicalUrl + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + body + "\n";
    }


    /**
     * ??????
     *
     * @param message
     * @param keyPath
     * @return
     * @throws Exception
     */
    public static String sign(byte[] message, String keyPath) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(getPrivateKey(keyPath));
        sign.update(message);
        return Base64.encodeBase64String(sign.sign());
    }

    /**
     * ???????????????
     *
     * @param filename ??????????????????  (required)
     * @return ????????????
     */
    public static PrivateKey getPrivateKey(String filename) {
        String content = getContent(filename);
        try {
            String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("??????Java???????????????RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("?????????????????????");
        }
    }

    /**
     * ??????Cipher???????????????
     * @param certificate
     * @return
     */
    public static Cipher getCipher(X509Certificate certificate) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());
            return cipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("??????Java???????????????RSA v1.5/OAEP", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("???????????????", e);
        }
    }

    /**
     * ??????Cipher??????
     *
     * @param message
     * @param cipher  ??????getcipher????????????
     * @return
     * @throws IllegalBlockSizeException
     */
    public static String rsaEncryptOAEP(String message, Cipher cipher) throws IllegalBlockSizeException {
        try {
            if (StringUtils.isBlank(message)) {
                return null;
            }
            byte[] data = message.getBytes(StandardCharsets.UTF_8);
            byte[] cipherdata = cipher.doFinal(data);
            return Base64.encodeBase64String(cipherdata);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalBlockSizeException("?????????????????????????????????214??????");
        }
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param message
     * @param certificate
     * @return
     * @throws IllegalBlockSizeException
     * @throws IOException
     */
    public static String rsaEncryptOAEP(String message, X509Certificate certificate) throws IllegalBlockSizeException, IOException {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());

            byte[] data = message.getBytes("utf-8");
            byte[] cipherdata = cipher.doFinal(data);
            return java.util.Base64.getEncoder().encodeToString(cipherdata);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("??????Java???????????????RSA v1.5/OAEP", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("???????????????", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalBlockSizeException("?????????????????????????????????214??????");
        }
    }

    /**
     * ?????????????????????(?????????????????????????????????
     *
     * @param message
     * @return
     * @throws IllegalBlockSizeException
     * @throws IOException
     */
    public static String rsaEncryptOAEP(String message) {
        //return rsaEncryptOAEP(message, getCertificate(MCH_PUB_KEY_PATH));
        final X509Certificate certificate = getCertificate(WECHATPAY_PUB_KEY_PATH);
        final String eo;
        try {
            eo = rsaEncryptOAEP(message, certificate);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("???????????????" + e);
        } catch (IOException e) {
            throw new RuntimeException("???????????????" + e);
        }
        return eo;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param ciphertext
     * @param privateKey
     * @return
     * @throws BadPaddingException
     * @throws IOException
     */
    public static String rsaDecryptOAEP(String ciphertext, PrivateKey privateKey) throws BadPaddingException, IOException {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] data = java.util.Base64.getDecoder().decode(ciphertext);
            return new String(cipher.doFinal(data), "utf-8");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new RuntimeException("??????Java???????????????RSA v1.5/OAEP", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("???????????????", e);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            throw new BadPaddingException("????????????");
        }
    }

    /**
     * ??????????????????
     *
     * @param message
     * @return
     * @throws IOException
     * @throws IllegalBlockSizeException
     */
    public static String rsaDecryptOAEP(String message) {
        try {
            return rsaDecryptOAEP(message, getPrivateKey(MCH_PRI_KEY_PATH));
        } catch (BadPaddingException e) {
            throw new RuntimeException("????????????:",e);
        } catch (IOException e) {
            throw new RuntimeException("????????????:",e);
        }
    }

    /**
     * ??????????????????
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String getContent(String path) {
        ClassPathResource resource = new ClassPathResource(path);
        InputStream inputStream = null;
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            inputStream = resource.getInputStream();
            br = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String data = new String(sb);
        return data;
    }

    public String sendGet(String url){
        //???????????????????????????
        String serialNo = WeChatPayUtil.getSerialNo(WeChatPayUtil.MCH_PUB_KEY_PATH);

        //??????????????????, ???????????????????????????
        serialNo = WeChatPayUtil.getSerialNo(WeChatPayUtil.WECHATPAY_PUB_KEY_PATH);

        //?????????????????????????????????????????????
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Wechatpay-Serial", serialNo);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("user-agent", WeChatPayUtil.DEFAULT_USER_AGENT);

        CloseableHttpClient httpClient = getVerifyHttpClient();

        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
        } catch (IOException e) {
            throw new RuntimeException("???????????????"+e);
        }

        //??????????????????????????????
        HttpEntity httpResponseEntity = httpResponse.getEntity();
        String responseEntityStr = null;
        try {
            responseEntityStr = EntityUtils.toString(httpResponseEntity);
        } catch (IOException e) {
            throw new RuntimeException("????????????????????????"+e);
        }
        try {
            httpResponse.close();
        } catch (IOException e) {
            logger.error("????????????????????????"+e);
        }

        return responseEntityStr;
    }

    public String sendGet(String endArg, String url) throws IOException {
        url = url + "/" + endArg;
        return sendGet(url);
    }

    public String sendGet1(String arg,String url) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
        url = url + "/" + arg;

        //???????????????????????????
        String serialNo = WeChatPayUtil.getSerialNo(WeChatPayUtil.MCH_PUB_KEY_PATH);
        String token = WeChatPayUtil.getToken("GET", url, null, WeChatPayUtil.MCH_ID, serialNo);
        String authorization = "WECHATPAY2-SHA256-RSA2048 " + token;

        //??????????????????, ???????????????????????????
        serialNo = WeChatPayUtil.getSerialNo(WeChatPayUtil.WECHATPAY_PUB_KEY_PATH);

        //?????????????????????????????????????????????
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Wechatpay-Serial", serialNo);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("user-agent", WeChatPayUtil.DEFAULT_USER_AGENT);
        httpGet.setHeader("Authorization", authorization);

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        CloseableHttpClient httpClient = httpClientBuilder.build();

        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        //??????????????????????????????
        HttpEntity httpResponseEntity = httpResponse.getEntity();
        String responseEntityStr = EntityUtils.toString(httpResponseEntity);
        httpResponse.close();

        return responseEntityStr;
    }

    public String sendPost(String requestParams, String url) {
        //????????????????????????????????????????????????
        String serialNo = WeChatPayUtil.getSerialNo(WeChatPayUtil.WECHATPAY_PUB_KEY_PATH);

        //?????????????????????????????????????????????
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Wechatpay-Serial", serialNo);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        //httpPost.setHeader("user-agent", WeChatPayWrapper.DEFAULT_USER_AGENT);
        httpPost.setEntity(new StringEntity(requestParams, ContentType.create("application/json", "utf-8")));
        CloseableHttpClient httpClient = getVerifyHttpClient();
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPost);
        } catch (IOException e) {
            throw new RuntimeException("???????????????" + e);
        }

        //??????????????????
        HttpEntity httpResponseEntity = httpResponse.getEntity();

        String responseEntityStr = null;
        try {
            responseEntityStr = EntityUtils.toString(httpResponseEntity);
        } catch (IOException e) {
            throw new RuntimeException("????????????????????????" + e);
        }
        try {
            httpResponse.close();
        } catch (IOException e) {
            logger.error("??????????????????" + e);
        }

        return responseEntityStr;
    }

    public String sendPost1(String requestParams, String url) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //???????????????????????????
        String serialNo = WeChatPayUtil.getSerialNo(WeChatPayUtil.MCH_PUB_KEY_PATH);

        String token = WeChatPayUtil.getToken("POST", url, requestParams, MCH_ID, serialNo);
        String authorization = "WECHATPAY2-SHA256-RSA2048 " + token;

        //????????????????????????????????????????????????
        serialNo = WeChatPayUtil.getSerialNo(WeChatPayUtil.WECHATPAY_PUB_KEY_PATH);

        //?????????????????????????????????????????????
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Wechatpay-Serial", serialNo);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("user-agent", WeChatPayUtil.DEFAULT_USER_AGENT);
        //?????????????????????HttpClientBuilder?????? httpclient ??????????????????
        httpPost.setHeader("Authorization", authorization);
        httpPost.setEntity(new StringEntity(requestParams, ContentType.create("application/json", "utf-8")));

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        CloseableHttpClient httpClient = httpClientBuilder.build();


        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

        //??????????????????
        HttpEntity httpResponseEntity = httpResponse.getEntity();

        String responseEntityStr = EntityUtils.toString(httpResponseEntity);
        httpResponse.close();

        return responseEntityStr;
    }

    private static CloseableHttpClient getVerifyHttpClient() {
        ArrayList<X509Certificate> certificates = new ArrayList<>();
        certificates.add(getCertificate(WECHATPAY_PUB_KEY_PATH));

        CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(MCH_ID, getSerialNo(MCH_PUB_KEY_PATH), getPrivateKey(MCH_PRI_KEY_PATH))
                .withWechatpay(certificates)
                .build();

        return httpClient;
    }
}
