package com.lex.bouncy.castle

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * MD校验
 */
public class MDCoderTest {

    /**
     * 测试Tiger
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeTiger() throws Exception {
        String str = "Tiger消息摘要";

        // 获得摘要信息
        byte[] data1 = MDCoder.encodeTiger(str.getBytes());
        byte[] data2 = MDCoder.encodeTiger(str.getBytes());

        // 校验
        assertArrayEquals(data1, data2);
    }

    /**
     * 测试RipeMD320Hex
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeTigerHex() throws Exception {
        String str = "TigerHex消息摘要";

        // 获得摘要信息
        String data1 = MDCoder.encodeTigerHex(str.getBytes());
        String data2 = MDCoder.encodeTigerHex(str.getBytes());

        System.err.println("原文：\t" + str);

        System.err.println("TigerHex-1：\t" + data1);
        System.err.println("TigerHex-2：\t" + data2);

        // 校验
        assertEquals(data1, data2);
    }

    /**
     * 测试Whirlpool
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeWhirlpool() throws Exception {
        String str = "Tiger消息摘要";

        // 获得摘要信息
        byte[] data1 = MDCoder.encodeWhirlpool(str.getBytes());
        byte[] data2 = MDCoder.encodeWhirlpool(str.getBytes());

        // 校验
        assertArrayEquals(data1, data2);
    }

    /**
     * 测试WhirlpoolHex
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeWhirlpoolHex() throws Exception {
        String str = "WhirlpoolHex消息摘要";

        // 获得摘要信息
        String data1 = MDCoder.encodeWhirlpoolHex(str.getBytes());
        String data2 = MDCoder.encodeWhirlpoolHex(str.getBytes());

        System.err.println("原文：\t" + str);

        System.err.println("WhirlpoolHex-1：\t" + data1);
        System.err.println("WhirlpoolHex-2：\t" + data2);

        // 校验
        assertEquals(data1, data2);
    }

    /**
     * 测试GOST3411
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeGOST3411() throws Exception {
        String str = "GOST3411消息摘要";

        // 获得摘要信息
        byte[] data1 = MDCoder.encodeGOST3411(str.getBytes());
        byte[] data2 = MDCoder.encodeGOST3411(str.getBytes());

        // 校验
        assertArrayEquals(data1, data2);
    }

    /**
     * 测试GOST3411Hex
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeGOST3411Hex() throws Exception {
        String str = "GOST3411Hex消息摘要";

        // 获得摘要信息
        String data1 = MDCoder.encodeGOST3411Hex(str.getBytes());
        String data2 = MDCoder.encodeGOST3411Hex(str.getBytes());

        System.err.println("原文：\t" + str);

        System.err.println("GOST3411Hex-1：\t" + data1);
        System.err.println("GOST3411Hex-2：\t" + data2);

        // 校验
        assertEquals(data1, data2);
    }
}
