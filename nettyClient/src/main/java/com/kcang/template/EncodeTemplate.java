package com.kcang.template;

import io.netty.handler.codec.MessageToMessageEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * 用于出口消息解码处理的统一模板类
 */
public abstract class EncodeTemplate extends MessageToMessageEncoder<String> {

    //偏移量设置
    private final String IV_STRING = "com.kcang.aes.ed";
    private final String encoding = "UTF-8";

    /**
     * AES加密函数
     * @param content 待加密明文
     * @param key 密钥
     * @return 返回加密后密文
     * @throws Exception 异常
     */
    protected String encryptAES(String content, String key) throws Exception{
        byte[] byteContent = content.getBytes(encoding);
        byte[] enCodeFormat = key.getBytes(encoding);
        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
        byte[] initParam = IV_STRING.getBytes(encoding);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
        // 指定加密的算法、工作模式和填充方式
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(byteContent);
        // 同样对加密后数据进行 base64 编码
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

}
