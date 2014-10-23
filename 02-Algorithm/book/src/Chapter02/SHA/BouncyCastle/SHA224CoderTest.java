package com.lex.bouncy.castle;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * SHA-224校验
 */
public class SHA224CoderTest {

    /**
     * 测试SHA-224
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeSHA224() throws Exception {
        String str = "SHA224消息摘要";

        // 获得摘要信息
        byte[] data1 = SHA224Coder.encodeSHA224(str.getBytes());
        byte[] data2 = SHA224Coder.encodeSHA224(str.getBytes());

        // 校验
        assertArrayEquals(data1, data2);
    }

    /**
     * 测试SHA-224Hex
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeSHA224Hex() throws Exception {
        String str = "SHA224Hex消息摘要";

        // 获得摘要信息
        String data1 = SHA224Coder.encodeSHA224Hex(str.getBytes());
        String data2 = SHA224Coder.encodeSHA224Hex(str.getBytes());

        System.err.println("原文：\t" + str);

        System.err.println("SHA224Hex-1：\t" + data1);
        System.err.println("SHA224Hex-2：\t" + data2);

        // 校验
        assertEquals(data1, data2);
    }
}
