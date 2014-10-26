package com.lex.dsa;

import static org.junit.Assert.*;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.junit.Before;
import org.junit.Test;

/**
 * DSA签名校验
 */
public class DSACoderTest {

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

        Map<String, Object> keyMap = DSACoder.initKey();

        publicKey = DSACoder.getPublicKey(keyMap);

        privateKey = DSACoder.getPrivateKey(keyMap);

        System.err.println("公钥: \n" + Base64.encodeBase64String(publicKey));
        System.err.println("私钥： \n" + Base64.encodeBase64String(privateKey));
    }

    /**
     * 校验签名
     *
     * @throws Exception
     */
    @Test
    public void test() throws Exception {

        String inputStr = "DSA数字签名";
        byte[] data = inputStr.getBytes();

        // 产生签名
        byte[] sign = DSACoder.sign(data, privateKey);
        System.err.println("签名:\r" + Hex.encodeHexString(sign));

        // 验证签名
        boolean status = DSACoder.verify(data, publicKey, sign);
        System.err.println("状态:\r" + status);
        assertTrue(status);

    }

}
