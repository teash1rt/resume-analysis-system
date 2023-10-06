package com.springboot.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieManager {
    public static void setCookie(String key, Object value, Integer max_age, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, (String) value);
        cookie.setMaxAge(max_age);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
