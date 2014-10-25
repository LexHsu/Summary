package com.lex.rsa

import static org.junit.Assert.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * RSA数字签名校验
 */
public class RSACoderTest {

    /**
     * 公钥
     */
    private byte[] publicKey;

    /**
     * 私钥
     */
    private byte[] privateKey;

    /**
     * 初始化密钥
     *
     * @throws Exception
     */
    @Before
    public void initKey() throws Exception {

        Map<String, Object> keyMap = RSACoder.initKey();

        publicKey = RSACoder.getPublicKey(keyMap);

        privateKey = RSACoder.getPrivateKey(keyMap);

        System.err.println("公钥: \n" + Base64.encodeBase64String(publicKey));
        System.err.println("私钥： \n" + Base64.encodeBase64String(privateKey));
    }

    /**
     * 校验
     *
     * @throws Exception
     */
    @Test
    public void testSign() throws Exception {

        String inputStr = "RSA数字签名";
        byte[] data = inputStr.getBytes();

        // 产生签名
        byte[] sign = RSACoder.sign(data, privateKey);
        System.err.println("签名:\n" + Hex.encodeHexString(sign));

        // 验证签名
        boolean status = RSACoder.verify(data, publicKey, sign);
        System.err.println("状态:\n" + status);
        assertTrue(status);

    }

}
