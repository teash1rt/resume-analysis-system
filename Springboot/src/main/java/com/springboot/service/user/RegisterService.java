package com.springboot.service.user;

import com.springboot.common.R;

import javax.servlet.http.HttpSession;

public interface RegisterService {
    R register(String email, String username, String password, String verify_code, HttpSession httpSession);
}
