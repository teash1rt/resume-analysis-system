package com.springboot.service.user;

import com.springboot.common.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public interface RegisterService {
    R register(String email, String username, String password, String verify_code, HttpSession httpSession);

    void getVerifyCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException;

}
