package com.springboot.controller.user;

import com.springboot.common.R;
import com.springboot.dto.RegisterDTO;
import com.springboot.service.user.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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

    @GetMapping("/get-verify-code/")
    public void getVerifyCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        registerService.getVerifyCode(httpServletRequest, httpServletResponse);
    }
}
