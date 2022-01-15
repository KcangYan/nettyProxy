package com.kcang.template;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * 用于入口消息解码器统一模板
 */
public abstract class DecodeTemplate extends ByteToMessageDecoder {

    //偏移量设置
    private final String IV_STRING = "com.kcang.aes.ed";
    private final String encoding = "UTF-8";
    /**
     * AES解密函数
     * @param content 待解密密文
     * @param key 密钥
     * @return 返回明文
     * @throws Exception 异常
     */
    protected String decryptAES(String content, String key) throws Exception {
        // base64 解码
        byte[] encryptedBytes = Base64.getDecoder().decode(content);

        byte[] enCodeFormat = key.getBytes(encoding);
        SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");
        byte[] initParam = IV_STRING.getBytes(encoding);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] result = cipher.doFinal(encryptedBytes);
        return new String(result, encoding);
    }
}
