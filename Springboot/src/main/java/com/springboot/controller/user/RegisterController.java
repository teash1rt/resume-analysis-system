package com.springboot.controller.user;

import com.springboot.common.R;
import com.springboot.service.user.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class RegisterController {
    private final RegisterService registerService;

    @PostMapping("/register/")
    public R register(@RequestBody Map<String, String> map, HttpSession httpSession) {
        String email = map.get("email");
        String username = map.get("username");
        String password = map.get("password");
        String verify_code = map.get("verify_code");
        return registerService.register(email, username, password, verify_code, httpSession);
    }
}
