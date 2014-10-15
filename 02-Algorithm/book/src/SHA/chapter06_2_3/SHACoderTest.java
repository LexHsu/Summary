package com.lex.commons.codec;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * SHAHex校验
 */
public class SHACoderTest {

    /**
     * 测试SHA-1
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeSHA() throws Exception {
        String str = "SHA1消息摘要";

        // 获得摘要信息
        byte[] data1 = SHACoder.encodeSHA(str);
        byte[] data2 = SHACoder.encodeSHA(str);

        // 校验
        assertArrayEquals(data1, data2);
    }

    /**
     * 测试SHA-1Hex
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeSHAHex() throws Exception {
        String str = "SHA-1Hex消息摘要";

        // 获得摘要信息
        String data1 = SHACoder.encodeSHAHex(str);
        String data2 = SHACoder.encodeSHAHex(str);

        System.err.println("原文：\t" + str);

        System.err.println("SHA1Hex-1：\t" + data1);
        System.err.println("SHA1Hex-2：\t" + data2);

        // 校验
        assertEquals(data1, data2);
    }

    /**
     * 测试SHA-256
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeSHA256() throws Exception {
        String str = "SHA256消息摘要";

        // 获得摘要信息
        byte[] data1 = SHACoder.encodeSHA256(str);
        byte[] data2 = SHACoder.encodeSHA256(str);

        // 校验
        assertArrayEquals(data1, data2);
    }

    /**
     * 测试SHA-256Hex
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeSHA256Hex() throws Exception {
        String str = "SHA256Hex消息摘要";

        // 获得摘要信息
        String data1 = SHACoder.encodeSHA256Hex(str);
        String data2 = SHACoder.encodeSHA256Hex(str);

        System.err.println("原文：\t" + str);

        System.err.println("SHA256Hex-1：\t" + data1);
        System.err.println("SHA256Hex-2：\t" + data2);

        // 校验
        assertEquals(data1, data2);
    }

    /**
     * 测试SHA-384
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeSHA384() throws Exception {
        String str = "SHA384消息摘要";

        // 获得摘要信息
        byte[] data1 = SHACoder.encodeSHA384(str);
        byte[] data2 = SHACoder.encodeSHA384(str);

        // 校验
        assertArrayEquals(data1, data2);
    }

    /**
     * 测试SHA-384Hex
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeSHA384Hex() throws Exception {
        String str = "SHA384Hex消息摘要";

        // 获得摘要信息
        String data1 = SHACoder.encodeSHA384Hex(str);
        String data2 = SHACoder.encodeSHA384Hex(str);

        System.err.println("原文：\t" + str);

        System.err.println("SHA384Hex-1：\t" + data1);
        System.err.println("SHA384Hex-2：\t" + data2);

        // 校验
        assertEquals(data1, data2);
    }

    /**
     * 测试SHA-512
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeSHA512() throws Exception {
        String str = "SHA512消息摘要";

        // 获得摘要信息
        byte[] data1 = SHACoder.encodeSHA512(str);
        byte[] data2 = SHACoder.encodeSHA512(str);

        // 校验
        assertArrayEquals(data1, data2);
    }

    /**
     * 测试SHA-512Hex
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeSHA512Hex() throws Exception {
        String str = "SHA512Hex消息摘要";

        // 获得摘要信息
        String data1 = SHACoder.encodeSHA512Hex(str);
        String data2 = SHACoder.encodeSHA512Hex(str);

        System.err.println("原文：\t" + str);

        System.err.println("SHA512Hex-1：\t" + data1);
        System.err.println("SHA512Hex-2：\t" + data2);

        // 校验
        assertEquals(data1, data2);
    }

}
