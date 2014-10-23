package com.lex.bouncy.castle

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * RipeMD校验
 */
public class RipeMDCoderTest {

    /**
     * 测试RipeMD128
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeRipeMD128() throws Exception {

        String str = "RipeMD128消息摘要";

        // 获得摘要信息
        byte[] data1 = RipeMDCoder.encodeRipeMD128(str.getBytes());
        byte[] data2 = RipeMDCoder.encodeRipeMD128(str.getBytes());

        // 校验
        assertArrayEquals(data1, data2);
    }

    /**
     * 测试RipeMD128Hex
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeRipeMD128Hex() throws Exception {

        String str = "RipeMD128Hex消息摘要";

        // 获得摘要信息
        String data1 = RipeMDCoder.encodeRipeMD128Hex(str.getBytes());
        String data2 = RipeMDCoder.encodeRipeMD128Hex(str.getBytes());

        System.err.println("原文：\t" + str);

        System.err.println("RipeMD128Hex-1：\t" + data1);
        System.err.println("RipeMD128Hex-2：\t" + data2);

        // 校验
        assertEquals(data1, data2);
    }

    /**
     * 测试RipeMD160
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeRipeMD160() throws Exception {

        String str = "RipeMD160消息摘要";

        // 获得摘要信息
        byte[] data1 = RipeMDCoder.encodeRipeMD160(str.getBytes());
        byte[] data2 = RipeMDCoder.encodeRipeMD160(str.getBytes());

        // 校验
        assertArrayEquals(data1, data2);
    }

    /**
     * 测试RipeMD160Hex
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeRipeMD160Hex() throws Exception {

        String str = "RipeMD160Hex消息摘要";

        // 获得摘要信息
        String data1 = RipeMDCoder.encodeRipeMD160Hex(str.getBytes());
        String data2 = RipeMDCoder.encodeRipeMD160Hex(str.getBytes());

        System.err.println("原文：\t" + str);

        System.err.println("RipeMD160Hex-1：\t" + data1);
        System.err.println("RipeMD160Hex-2：\t" + data2);

        // 校验
        assertEquals(data1, data2);
    }

    /**
     * 测试RipeMD256
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeRipeMD256() throws Exception {

        String str = "RipeMD256消息摘要";

        // 获得摘要信息
        byte[] data1 = RipeMDCoder.encodeRipeMD256(str.getBytes());
        byte[] data2 = RipeMDCoder.encodeRipeMD256(str.getBytes());

        // 校验
        assertArrayEquals(data1, data2);
    }

    /**
     * 测试RipeMD256Hex
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeRipeMD256Hex() throws Exception {

        String str = "RipeMD256Hex消息摘要";

        // 获得摘要信息
        String data1 = RipeMDCoder.encodeRipeMD256Hex(str.getBytes());
        String data2 = RipeMDCoder.encodeRipeMD256Hex(str.getBytes());

        System.err.println("原文：\t" + str);

        System.err.println("RipeMD256Hex-1：\t" + data1);
        System.err.println("RipeMD256Hex-2：\t" + data2);

        // 校验
        assertEquals(data1, data2);
    }

    /**
     * 测试RipeMD320
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeRipeMD320() throws Exception {

        String str = "RipeMD320消息摘要";

        // 获得摘要信息
        byte[] data1 = RipeMDCoder.encodeRipeMD320(str.getBytes());
        byte[] data2 = RipeMDCoder.encodeRipeMD320(str.getBytes());

        // 校验
        assertArrayEquals(data1, data2);
    }

    /**
     * 测试RipeMD320Hex
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeRipeMD320Hex() throws Exception {

        String str = "RipeMD320Hex消息摘要";

        // 获得摘要信息
        String data1 = RipeMDCoder.encodeRipeMD320Hex(str.getBytes());
        String data2 = RipeMDCoder.encodeRipeMD320Hex(str.getBytes());

        System.err.println("原文：\t" + str);

        System.err.println("RipeMD320Hex-1：\t" + data1);
        System.err.println("RipeMD320Hex-2：\t" + data2);

        // 校验
        assertEquals(data1, data2);
    }

}
