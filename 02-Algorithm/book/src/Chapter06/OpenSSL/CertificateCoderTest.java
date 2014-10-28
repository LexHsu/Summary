package com.lex.openssl;

import static org.junit.Assert.*;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

/**
 * 证书校验
 */
public class CertificateCoderTest {

    private String password = "123456";

    private String alias = "1";

    private String certificatePath = "d:/ca/certs/ca.cer";

    private String keyStorePath = "d:/ca/certs/ca.p12";

    /**
     * 公钥加密——私钥解密
     *
     * @throws Exception
     */
    @Test
    public void test1() {

        try {
            System.err.println("公钥加密——私钥解密");
            String inputStr = "Ceritifcate";
            byte[] data = inputStr.getBytes();

            // 公钥加密
            byte[] encrypt = CertificateCoder.encryptByPublicKey(data,
                    certificatePath);

            // 私钥解密
            byte[] decrypt = CertificateCoder.decryptByPrivateKey(encrypt,
                    keyStorePath, alias, password);

            String outputStr = new String(decrypt);

            System.err.println("加密前:\n" + inputStr);

            System.err.println("解密后:\n" + outputStr);

            // 验证数据一致
            assertArrayEquals(data, decrypt);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    /**
     * 私钥加密——公钥解密
     *
     * @throws Exception
     */
    @Test
    public void test2() {

        System.err.println("私钥加密——公钥解密");

        String inputStr = "sign";
        byte[] data = inputStr.getBytes();

        try {
            // 私钥加密
            byte[] encodedData = CertificateCoder.encryptByPrivateKey(data,
                    keyStorePath, alias, password);

            // 公钥加密
            byte[] decodedData = CertificateCoder.decryptByPublicKey(
                    encodedData, certificatePath);

            String outputStr = new String(decodedData);

            System.err.println("加密前:\n" + inputStr);
            System.err.println("解密后:\n" + outputStr);

            // 校验
            assertEquals(inputStr, outputStr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * 签名验证
     *
     * @throws Exception
     */
    @Test
    public void testSign() {

        try {
            String inputStr = "签名";
            byte[] data = inputStr.getBytes();
            System.err.println("私钥签名——公钥验证");

            // 产生签名
            byte[] sign = CertificateCoder.sign(data, keyStorePath, alias,
                    password,certificatePath);
            System.err.println("签名:\n" + Hex.encodeHexString(sign));

            // 验证签名
            boolean status = CertificateCoder.verify(data, sign,
                    certificatePath);
            System.err.println("状态:\n" + status);

            // 校验
            assertTrue(status);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

}
