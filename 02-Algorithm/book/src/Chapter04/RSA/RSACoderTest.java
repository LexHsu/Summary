package com.lex.rsa;

import static org.junit.Assert.*;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * RSA校验
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

        // 初始化密钥
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
    public void test() throws Exception {

        System.err.println("\n---私钥加密——公钥解密---");

        String inputStr1 = "RSA加密算法";
        byte[] data1 = inputStr1.getBytes();
        System.err.println("原文:\n" + inputStr1);

        // 加密
        byte[] encodedData1 = RSACoder.encryptByPrivateKey(data1, privateKey);
        System.err.println("加密后:\n" + Base64.encodeBase64String(encodedData1));

        // 解密
        byte[] decodedData1 = RSACoder.decryptByPublicKey(encodedData1,
                publicKey);
        String outputStr1 = new String(decodedData1);
        System.err.println("解密后:\n" + outputStr1);

        // 校验
        assertEquals(inputStr1, outputStr1);

        System.err.println("\n---公钥加密——私钥解密---");
        String inputStr2 = "RSA Encypt Algorithm";
        byte[] data2 = inputStr2.getBytes();
        System.err.println("原文:\n" + inputStr2);

        // 加密
        byte[] encodedData2 = RSACoder.encryptByPublicKey(data2, publicKey);
        System.err.println("加密后:\n" + Base64.encodeBase64String(encodedData2));

        // 解密
        byte[] decodedData2 = RSACoder.decryptByPrivateKey(encodedData2,
                privateKey);
        String outputStr2 = new String(decodedData2);
        System.err.println("解密后: " + outputStr2);

        // 校验
        assertEquals(inputStr2, outputStr2);
    }

}
