package com.springboot.utils;

public class VerifyArgsUtils {
    public static boolean is_empty(String... args) {
        for (String s : args) {
            if (s == null || s.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
