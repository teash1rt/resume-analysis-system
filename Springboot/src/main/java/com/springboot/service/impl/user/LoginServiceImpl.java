package com.springboot.service.impl.user;

import com.springboot.common.R;
import com.springboot.entity.user.User;
import com.springboot.service.user.LoginService;
import com.springboot.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;

    @Override
    public R login(String email, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            User user = (User) authenticate.getPrincipal();
            String token = JwtUtils.generateToken(user);
            return R.success("登录成功", token);
        } catch (Exception e) {
            return R.error("用户名或密码错误");
        }
    }
}
