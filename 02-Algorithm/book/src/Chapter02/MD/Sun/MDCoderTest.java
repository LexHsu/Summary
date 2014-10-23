package com.lex.sun

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * MD校验
 *
 */
public class MDCoderTest {

    /**
     * 测试MD2
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeMD2() throws Exception {
        String str = "MD2消息摘要";

        // 获得摘要信息
        byte[] data1 = MDCoder.encodeMD2(str.getBytes());
        byte[] data2 = MDCoder.encodeMD2(str.getBytes());

        // 校验
        assertArrayEquals(data1, data2);

    }

    /**
     * 测试MD5
     *
     * @throws Exception
     */
    @Test
    public final void testEncodeMD5() throws Exception {
        String str = "MD5消息摘要";

        // 获得摘要信息
        byte[] data1 = MDCoder.encodeMD5(str.getBytes());
        byte[] data2 = MDCoder.encodeMD5(str.getBytes());

        // 校验
        assertArrayEquals(data1, data2);
    }

}
