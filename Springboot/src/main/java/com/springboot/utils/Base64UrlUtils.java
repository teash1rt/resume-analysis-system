package com.springboot.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64UrlUtils {

    public static String encoding(String token) {
        byte[] originalBytes = token.getBytes(StandardCharsets.UTF_8);
        // 使用 Base64 URL 编码将字节数组转换为字符串 这样写会将 jwt 中不安全的字符(/ - +等)替换掉
        return Base64.getUrlEncoder().withoutPadding().encodeToString(originalBytes);
    }

    public static String decoding(String url_path) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(url_path);
        // 使用 UTF-8 解码将字节数组转换为原始 token
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
