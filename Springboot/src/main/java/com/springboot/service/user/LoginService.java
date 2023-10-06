package com.springboot.service.user;

import com.springboot.common.R;

import javax.servlet.http.HttpServletResponse;

public interface LoginService {
    R login(String email, String password);
}
