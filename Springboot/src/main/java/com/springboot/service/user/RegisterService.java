package com.springboot.service.user;

import com.springboot.common.R;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface RegisterService {
    R register(String email, String password, String username, String verify_code, HttpSession httpSession);
}
