package com.springboot.service.user;

import com.springboot.common.R;

public interface TokenCheckService {
    R visitor_token_check(String token);

    R url_token_check(String token);
}
