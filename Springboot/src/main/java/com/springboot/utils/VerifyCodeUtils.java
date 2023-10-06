package com.springboot.utils;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class VerifyCodeUtils {
    public static void createCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(153, 69, 4, 5);
        captcha.write(httpServletResponse.getOutputStream());
        // 获得生成的验证码字符
        String code = captcha.getCode();
        httpServletRequest.getSession().setAttribute("sessionVerifyCode", code);
    }

    public static boolean checkCode(String code, HttpSession httpSession) {
        String verifyCode = (String) httpSession.getAttribute("sessionVerifyCode");
        return verifyCode.equals(code);
    }
}
