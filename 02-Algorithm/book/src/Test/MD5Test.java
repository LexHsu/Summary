package com.lex.md

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

/**
 * 消息摘要编码测试<br>
 * 用于校验文件的MD5值
 *
 * <pre>
 * 文件为 mysql-essential-5.1.38-win32.msi
 * 存放于D盘根目录
 * MD5值为5a077abefee447cbb271e2aa7f6d5a47
 * </pre>
 */
public class MD5Test {

    /**
     * 验证文件的MD5值
     *
     * @throws Exception
     */
    @Test
    public void testByMessageDigest() throws Exception {
        // 文件路径
        String path = "D:\\mysql-essential-5.1.38-win32.msi";

        // 构建文件输入流
        FileInputStream fis = new FileInputStream(new File(path));

        // 初始化MessageDigest，并指定MD5算法
        DigestInputStream dis = new DigestInputStream(fis, MessageDigest
                .getInstance("MD5"));

        // 流缓冲大小
        int buf = 1024;

        // 缓冲字节数组
        byte[] buffer = new byte[buf];

        // 当读到值大于-1就继续读
        int read = dis.read(buffer, 0, buf);

        while (read > -1) {
            read = dis.read(buffer, 0, buf);
        }

        // 关闭流
        dis.close();

        // 获得MessageDigest
        MessageDigest md = dis.getMessageDigest();

        // 摘要处理
        byte[] b = md.digest();

        // 十六进制转换
        String md5hex = Hex.encodeHexString(b);

        // 验证
        assertEquals(md5hex, "5a077abefee447cbb271e2aa7f6d5a47");
    }

    /**
     * 验证文件的MD5值
     *
     * @throws Exception
     */
    @Test
    public void testByDigestUtils() throws Exception {
        // 文件路径
        String path = "D:\\mysql-essential-5.1.38-win32.msi";

        // 构建文件输入流
        FileInputStream fis = new FileInputStream(new File(path));

        // 使用DigestUtils做MD5Hex处理
        String md5hex = DigestUtils.md5Hex(fis);

        // 关闭流
        fis.close();

        // 验证
        assertEquals(md5hex, "5a077abefee447cbb271e2aa7f6d5a47");
    }

}
