package com.springboot.controller.user;

import com.springboot.service.user.VerifyCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 注册图形验证码
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class VerifyCodeController {
    private final VerifyCodeService verifyCodeService;

    @GetMapping("/get-verify-code/")
    public void get_verify_code(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        verifyCodeService.get_verify_code(httpServletRequest, httpServletResponse);
    }
}
