package com.lex.openssl;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;

/**
 * 证书组件
 */
public abstract class CertificateCoder {

    /**
     * 证书类型X509
     */
    public static final String CERT_TYPE = "X.509";

    /**
     * 密钥库类型PCKS12
     */
    private static final String STORE_TYPE = "PKCS12";

    /**
     * 由KeyStore获得私钥
     *
     * @param keyStorePath
     *            密钥库路径
     * @param alias
     *            别名
     * @param password
     *            密码
     * @return PrivateKey 私钥
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyByKeyStore(String keyStorePath,
            String alias, String password) throws Exception {

        // 获得密钥库
        KeyStore ks = getKeyStore(keyStorePath, password);

        // 获得私钥
        return (PrivateKey) ks.getKey(alias, password.toCharArray());

    }

    /**
     * 由Certificate获得公钥
     *
     * @param certificatePath
     *            证书路径
     * @return PublicKey 公钥
     * @throws Exception
     */
    public static PublicKey getPublicKeyByCertificate(String certificatePath)
            throws Exception {

        // 获得证书
        Certificate certificate = getCertificate(certificatePath);

        // 获得公钥
        return certificate.getPublicKey();

    }

    /**
     * 获得Certificate
     *
     * @param certificatePath
     *            证书路径
     * @return Certificate 证书
     * @throws Exception
     */
    private static X509Certificate getCertificate(String certificatePath)
            throws Exception {

        // 实例化证书工厂
        CertificateFactory certificateFactory = CertificateFactory
                .getInstance(CERT_TYPE);

        // 取得证书文件流
        FileInputStream in = new FileInputStream(certificatePath);

        // 生成证书
        Certificate certificate = certificateFactory.generateCertificate(in);

        // 关闭证书文件流
        in.close();

        return (X509Certificate) certificate;
    }

    /**
     * 获得KeyStore
     *
     * @param keyStorePath
     *            密钥库路径
     * @param password
     *            密码
     * @return KeyStore 密钥库
     * @throws Exception
     */
    private static KeyStore getKeyStore(String keyStorePath, String password)
            throws Exception {

        // 实例化密钥库
        KeyStore ks = KeyStore.getInstance(STORE_TYPE);

        // 获得密钥库文件流
        FileInputStream in = new FileInputStream(keyStorePath);
        // 加载密钥库
        ks.load(in, password.toCharArray());

        // 关闭密钥库文件流
        in.close();

        return ks;
    }

    /**
     * 私钥加密
     *
     * @param data
     *            待加密数据
     * @param keyStorePath
     *            密钥库路径
     * @param alias
     *            别名
     * @param password
     *            密码
     * @return byte[] 加密数据
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath,
            String alias, String password) throws Exception {

        // 取得私钥
        PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, alias,
                password);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(data);

    }

    /**
     * 私钥解密
     *
     * @param data
     *            待解密数据
     * @param keyStorePath
     *            密钥库路径
     * @param alias
     *            别名
     * @param password
     *            密码
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String keyStorePath,
            String alias, String password) throws Exception {

        // 取得私钥
        PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, alias,
                password);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);

    }

    /**
     * 公钥加密
     *
     * @param data
     *            待加密数据
     * @param certificatePath
     *            证书路径
     * @return byte[] 加密数据
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String certificatePath)
            throws Exception {

        // 取得公钥
        PublicKey publicKey = getPublicKeyByCertificate(certificatePath);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);

    }

    /**
     * 公钥解密
     *
     * @param data
     *            待解密数据
     * @param certificatePath
     *            证书路径
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String certificatePath)
            throws Exception {

        // 取得公钥
        PublicKey publicKey = getPublicKeyByCertificate(certificatePath);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(data);

    }

    /**
     * 签名
     *
     * @param keyStorePath
     *            密钥库路径
     * @param alias
     *            别名
     * @param password
     *            密码
     * @return byte[] 签名
     * @throws Exception
     */
    public static byte[] sign(byte[] sign, String keyStorePath, String alias,
            String password, String certificatePath) throws Exception {

        // 获得证书
        X509Certificate x509Certificate = getCertificate(certificatePath);

        // 构建签名，由证书指定签名算法
        Signature signature = Signature.getInstance(x509Certificate
                .getSigAlgName());

        // 获取私钥
        PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, alias,
                password);

        // 初始化签名，由私钥构建
        signature.initSign(privateKey);

        signature.update(sign);

        return signature.sign();
    }

    /**
     * 验证签名
     *
     * @param data
     *            数据
     * @param sign
     *            签名
     * @param certificatePath
     *            证书路径
     * @return boolean 验证通过为真
     * @throws Exception
     */
    public static boolean verify(byte[] data, byte[] sign,
            String certificatePath) throws Exception {

        // 获得证书
        X509Certificate x509Certificate = (X509Certificate) getCertificate(certificatePath);

        // 由证书构建签名
        Signature signature = Signature.getInstance(x509Certificate
                .getSigAlgName());

        // 由证书初始化签名，实际上是使用了证书中的公钥
        signature.initVerify(x509Certificate);

        signature.update(data);

        return signature.verify(sign);

    }

}
