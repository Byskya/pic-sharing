package com.example.picsharingspringboot.util;

import java.util.Base64;
//闲置中
public class Base64Utils {
    /**
     * 将byte数组转换成Base64字符串
     */
    public static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
    /**
     * 将Base64字符串转换成byte数组
     */
    public static byte[] decode(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}

