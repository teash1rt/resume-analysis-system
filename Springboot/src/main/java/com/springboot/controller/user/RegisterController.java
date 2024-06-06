package com.springboot.controller.user;

import com.springboot.common.R;
import com.springboot.dto.RegisterDTO;
import com.springboot.service.user.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class RegisterController {
    private final RegisterService registerService;

    @PostMapping("/register/")
    public R register(@RequestBody RegisterDTO registerDTO, HttpSession httpSession) {
        return registerService.register(
                registerDTO.getEmail(),
                registerDTO.getUsername(),
                registerDTO.getPassword(),
                registerDTO.getVerifyCode(),
                httpSession
        );
    }
}
