package com.springboot.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.common.R;
import com.springboot.entity.user.User;
import com.springboot.mapper.user.UserMapper;
import com.springboot.service.user.RegisterService;
import com.springboot.utils.JwtUtils;
import com.springboot.utils.VerifyArgsUtils;
import com.springboot.utils.VerifyCodeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public R register(String email, String username, String password, String verify_code, HttpSession httpSession) {
        if (VerifyArgsUtils.is_empty(email, username, password)) {
            return R.error("注册时各字段不能为空");
        } else if (!VerifyCodeUtils.checkCode(verify_code, httpSession)) {
            return R.error("验证码错误");
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email", email);
        if (userMapper.selectOne(userQueryWrapper) != null) {
            return R.error("邮箱已被注册");
        }
        User user = new User(null, email, username, passwordEncoder.encode(password), null, null, null);
        userMapper.insert(user);
        String token = JwtUtils.generateToken(user);
        return R.success("注册成功", token);
    }
}
