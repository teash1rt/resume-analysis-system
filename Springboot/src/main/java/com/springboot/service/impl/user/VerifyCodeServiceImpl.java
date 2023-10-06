package com.springboot.service.impl.user;

import com.springboot.service.user.VerifyCodeService;
import com.springboot.utils.VerifyCodeUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {
    @Override
    public void get_verify_code(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        VerifyCodeUtils.createCode(httpServletRequest, httpServletResponse);
    }
}
