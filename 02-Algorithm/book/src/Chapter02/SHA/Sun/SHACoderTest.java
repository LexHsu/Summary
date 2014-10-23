package com.lex.sun;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * SHA校验
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
        byte[] data1 = SHACoder.encodeSHA(str.getBytes());
        byte[] data2 = SHACoder.encodeSHA(str.getBytes());

        // 校验
        assertArrayEquals(data1, data2);
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
        byte[] data1 = SHACoder.encodeSHA256(str.getBytes());
        byte[] data2 = SHACoder.encodeSHA256(str.getBytes());

        // 校验
        assertArrayEquals(data1, data2);
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
        byte[] data1 = SHACoder.encodeSHA384(str.getBytes());
        byte[] data2 = SHACoder.encodeSHA384(str.getBytes());

        // 校验
        assertArrayEquals(data1, data2);
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
        byte[] data1 = SHACoder.encodeSHA512(str.getBytes());
        byte[] data2 = SHACoder.encodeSHA512(str.getBytes());

        // 校验
        assertArrayEquals(data1, data2);
    }
}
