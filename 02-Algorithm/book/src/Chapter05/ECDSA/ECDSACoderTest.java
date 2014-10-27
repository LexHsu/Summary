package com.lex.ecdsa;

import static org.junit.Assert.*;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.junit.Before;
import org.junit.Test;

/**
 * ECDSA数字签名校验
 */
public class ECDSACoderTest {

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

        Map<String, Object> keyMap = ECDSACoder.initKey();

        publicKey = ECDSACoder.getPublicKey(keyMap);

        privateKey = ECDSACoder.getPrivateKey(keyMap);

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

        String inputStr = "ECDSA 数字签名";
        byte[] data = inputStr.getBytes();

        // 产生签名
        byte[] sign = ECDSACoder.sign(data, privateKey);
        System.err.println("签名:\r" + Hex.encodeHexString(sign));

        // 验证签名
        boolean status = ECDSACoder.verify(data, publicKey, sign);
        System.err.println("状态:\r" + status);
        assertTrue(status);

    }

}
