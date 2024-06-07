package com.springboot.service.user;

import com.springboot.common.R;

public interface TokenCheckService {
    R visitorTokenCheck(String token);

    R urlTokenCheck(String token);
}
