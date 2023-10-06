package com.springboot.service.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface VerifyCodeService {
    void get_verify_code(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException;
}
