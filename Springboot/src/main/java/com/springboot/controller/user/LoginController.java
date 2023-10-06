package com.springboot.controller.user;

import com.springboot.common.R;
import com.springboot.service.user.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login/")
    public R login(@RequestBody Map<String, String> map) {
        String email = map.get("email");
        String password = map.get("password");
        return loginService.login(email, password);
    }
}
