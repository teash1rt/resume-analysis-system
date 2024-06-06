package com.springboot.controller.user;

import com.springboot.common.R;
import com.springboot.dto.LoginDTO;
import com.springboot.service.user.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login/")
    public R login(@RequestBody LoginDTO loginDTO) {
        return loginService.login(loginDTO.getEmail(), loginDTO.getPassword());
    }
}
