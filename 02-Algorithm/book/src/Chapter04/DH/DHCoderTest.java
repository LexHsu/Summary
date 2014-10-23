package com.lex.dh;

import static org.junit.Assert.*;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

/**
 * DH 校验
 *
 */
public class DHCoderTest {

    /**
     * 甲方公钥
     */
    private byte[] publicKey1;
    /**
     * 甲方私钥
     */
    private byte[] privateKey1;

    /**
     * 甲方本地密钥
     */
    private byte[] key1;

    /**
     * 乙方公钥
     */
    private byte[] publicKey2;

    /**
     * 乙方私钥
     */
    private byte[] privateKey2;

    /**
     * 乙方本地密钥
     */
    private byte[] key2;

    /**
     * 初始化密钥
     *
     * @throws Exception
     */
    @Before
    public final void initKey() throws Exception {

        // 生成甲方密钥对儿
        Map<String, Object> keyMap1 = DHCoder.initKey();

        publicKey1 = DHCoder.getPublicKey(keyMap1);
        privateKey1 = DHCoder.getPrivateKey(keyMap1);

        System.err.println("甲方公钥:\n" + Base64.encodeBase64String(publicKey1));
        System.err.println("甲方私钥:\n" + Base64.encodeBase64String(privateKey1));

        // 由甲方公钥产生本地密钥对儿
        Map<String, Object> keyMap2 = DHCoder.initKey(publicKey1);

        publicKey2 = DHCoder.getPublicKey(keyMap2);
        privateKey2 = DHCoder.getPrivateKey(keyMap2);

        System.err.println("乙方公钥:\n" + Base64.encodeBase64String(publicKey2));
        System.err.println("乙方私钥:\n" + Base64.encodeBase64String(privateKey2));

        key1 = DHCoder.getSecretKey(publicKey2, privateKey1);
        System.err.println("甲方本地密钥:\n" + Base64.encodeBase64String(key1));

        key2 = DHCoder.getSecretKey(publicKey1, privateKey2);
        System.err.println("乙方本地密钥:\n" + Base64.encodeBase64String(key2));

        assertArrayEquals(key1, key2);
    }

    /**
     * 校验
     *
     * @throws Exception
     */
    @Test
    public final void test() throws Exception {

        System.err.println("\n=====甲方向乙方发送加密数据=====");
        String input1 = "密码交换算法 ";
        System.err.println("原文: " + input1);

        System.err.println("---使用甲方本地密钥对数据加密---");

        // 使用甲方本地密钥对数据加密
        byte[] code1 = DHCoder.encrypt(input1.getBytes(), key1);

        System.err.println("加密: " + Base64.encodeBase64String(code1));

        System.err.println("---使用乙方本地密钥对数据解密---");

        // 使用乙方本地密钥对数据解密
        byte[] decode1 = DHCoder.decrypt(code1, key2);
        String output1 = (new String(decode1));

        System.err.println("解密: " + output1);

        assertEquals(input1, output1);

        System.err.println("\n=====乙方向甲方发送加密数据=====");
        String input2 = "DH";
        System.err.println("原文: " + input2);

        System.err.println("---使用乙方本地密钥对数据加密---");

        // 使用乙方本地密钥对数据加密
        byte[] code2 = DHCoder.encrypt(input2.getBytes(), key2);

        System.err.println("加密: " + Base64.encodeBase64String(code2));

        System.err.println("---使用甲方本地密钥对数据解密---");

        // 使用甲方本地密钥对数据解密
        byte[] decode2 = DHCoder.decrypt(code2, key1);
        String output2 = (new String(decode2));

        System.err.println("解密: " + output2);

        // 校验
        assertEquals(input2, output2);
    }
}
