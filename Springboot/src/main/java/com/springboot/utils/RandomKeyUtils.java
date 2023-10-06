package com.springboot.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomKeyUtils {
    public static String randomString(int len) {
        return RandomStringUtils.randomAlphanumeric(len);
    }

    public static String getKey() {
        return "Re" + randomString(18) + "Sume";
    }
}
