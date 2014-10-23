package com.lex.sun

import java.security.MessageDigest;

/**
 * MD加密组件
 *
 */
public abstract class MDCoder {

    /**
     * MD2加密
     *
     * @param data
     *            待加密数据
     * @return byte[] 消息摘要
     * @throws Exception
     */
    public static byte[] encodeMD2(byte[] data) throws Exception {
        // 初始化MessageDigest
        MessageDigest md = MessageDigest.getInstance("MD2");

        // 执行消息摘要
        return md.digest(data);
    }

    /**
     * MD5加密
     *
     * @param data
     *            待加密数据
     * @return byte[] 消息摘要
     *
     * @throws Exception
     */
    public static byte[] encodeMD5(byte[] data) throws Exception {
        // 初始化MessageDigest
        MessageDigest md = MessageDigest.getInstance("MD5");

        // 执行消息摘要
        return md.digest(data);
    }

}
