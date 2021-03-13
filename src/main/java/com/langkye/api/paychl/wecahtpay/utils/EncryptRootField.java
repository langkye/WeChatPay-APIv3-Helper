package com.langkye.api.paychl.wecahtpay.utils;

import com.langkye.api.paychl.wecahtpay.bean.*;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.cert.X509Certificate;

/**
 * 加密进件字段
 * @author langkye
 */
public class EncryptRootField {
    public static Root toEncrypt(Root root) throws IOException, IllegalBlockSizeException {
        /**
         * 加载微信证书
         */
        X509Certificate certificate = WeChatPayWrapper.getCertificate(WeChatPayWrapper.WECHATPAY_PUB_KEY_PATH);
        Cipher cipher = WeChatPayWrapper.getCipher(certificate);

        /**
         * 加密 超级管理员信息
         */
        encryptContactInfo(root,cipher);


        /**
         * 加密 主体信息
         */
        encryptSubjectInfo(root,cipher);


        /**
         * 结算银行
         */
        encryptBankAccountInfo(root,cipher);

        return root;
    }

    /**
     * 加密银行信息
     * @param root
     * @param cipher
     * @throws IllegalBlockSizeException
     */
    private static void encryptBankAccountInfo(Root root, Cipher cipher) throws IllegalBlockSizeException {
        BankAccountInfo bankAccountInfo = root.getBankAccountInfo();
        if (bankAccountInfo==null){
            return;
        }

        String accountName = bankAccountInfo.getAccountName();
        if (!StringUtils.isBlank(accountName)){
            bankAccountInfo.setAccountName(WeChatPayWrapper.rsaEncryptOAEP(accountName,cipher));
        }

        String accountNumber = bankAccountInfo.getAccountNumber();
        if (!StringUtils.isBlank(accountNumber)){
            bankAccountInfo.setAccountNumber(WeChatPayWrapper.rsaEncryptOAEP(accountNumber,cipher));
        }
        root.setBankAccountInfo(bankAccountInfo);
    }

    /**
     * 加密主体信息
     * @param root
     * @param cipher
     * @throws IllegalBlockSizeException
     */
    private static void encryptSubjectInfo(Root root, Cipher cipher) throws IllegalBlockSizeException {
        SubjectInfo subjectInfo = root.getSubjectInfo();
        if (subjectInfo==null){
            return;
        }

        //主体-法人
        IdentityInfo identityInfo = subjectInfo.getIdentityInfo();
        if (identityInfo!=null){
            //主体-法人-身份证
            IdCardInfo idCardInfo = identityInfo.getIdCardInfo();
            if (idCardInfo!=null){
                String idCardName = idCardInfo.getIdCardName();
                if (!StringUtils.isBlank(idCardName)){
                    idCardInfo.setIdCardName(WeChatPayWrapper.rsaEncryptOAEP(idCardName,cipher));
                }

                String idCardNumber = idCardInfo.getIdCardNumber();
                if (!StringUtils.isBlank(idCardNumber)){
                    idCardInfo.setIdCardNumber(WeChatPayWrapper.rsaEncryptOAEP(idCardNumber,cipher));
                }
                identityInfo.setIdCardInfo(idCardInfo);
            }


            //主体-法人-其他
            IdDocInfo idDocInfo = identityInfo.getIdDocInfo();
            if (idDocInfo!=null){
                String idDocName = idDocInfo.getIdDocName();
                if (!StringUtils.isBlank(idDocName)){
                    idDocInfo.setIdDocName(WeChatPayWrapper.rsaEncryptOAEP(idDocName,cipher));
                }

                String idDocNumber = idDocInfo.getIdDocNumber();
                if (!StringUtils.isBlank(idDocNumber)){
                    idDocInfo.setIdDocNumber(WeChatPayWrapper.rsaEncryptOAEP(idDocNumber,cipher));
                }

                identityInfo.setIdDocInfo(idDocInfo);
            }
            subjectInfo.setIdentityInfo(identityInfo);
        }





        //主体-最终受益人
        UboInfo uboInfo = subjectInfo.getUboInfo();
        uboInfo.setName(WeChatPayWrapper.rsaEncryptOAEP(uboInfo.getName(),cipher));
        uboInfo.setIdNumber(WeChatPayWrapper.rsaEncryptOAEP(uboInfo.getIdNumber(),cipher));

        subjectInfo.setUboInfo(uboInfo);
        root.setSubjectInfo(subjectInfo);
    }

    /**
     * 加密联系人信息
     * @param root
     * @param cipher
     * @throws IllegalBlockSizeException
     */
    private static void encryptContactInfo(Root root,Cipher cipher) throws IllegalBlockSizeException {
        ContactInfo contactInfo = root.getContactInfo();
        if (contactInfo == null){
            return;
        }

        String contactName = contactInfo.getContactName();
        if (!StringUtils.isBlank(contactName)){
            contactInfo.setContactName(WeChatPayWrapper.rsaEncryptOAEP(contactName,cipher));
        }

        String contactIdNumber = contactInfo.getContactIdNumber();
        if (!StringUtils.isBlank(contactIdNumber)){
            contactInfo.setContactIdNumber(WeChatPayWrapper.rsaEncryptOAEP(contactIdNumber,cipher));
        }

        String openid = contactInfo.getOpenid();
        if (!StringUtils.isBlank(openid)){
            contactInfo.setOpenid(WeChatPayWrapper.rsaEncryptOAEP(openid,cipher));
        }

        String mobilePhone = contactInfo.getMobilePhone();
        if (!StringUtils.isBlank(mobilePhone)){
            contactInfo.setMobilePhone(WeChatPayWrapper.rsaEncryptOAEP(mobilePhone,cipher));
        }

        String contactEmail = contactInfo.getContactEmail();
        if (!StringUtils.isBlank(contactEmail)){
            contactInfo.setContactEmail(WeChatPayWrapper.rsaEncryptOAEP(contactEmail,cipher));
        }

        root.setContactInfo(contactInfo);
    }
}
