package com.springboot.service.user;

import com.springboot.common.R;

public interface LoginService {
    R login(String email, String password);
}
