package com.springboot.service.impl.user;

import com.springboot.common.R;
import com.springboot.common.RedisBaseKey;
import com.springboot.service.user.TokenCheckService;
import com.springboot.utils.Base64UrlUtils;
import com.springboot.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@RequiredArgsConstructor
public class TokenCheckServiceImpl implements TokenCheckService {

    private final UserDetailsService userDetailsService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public R visitor_token_check(String token) {
        if (token == null) {
            return R.success("token查验完成", false);
        }
        String email = JwtUtils.extractEmail(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return R.success("token查验完成", JwtUtils.isTokenValid(token, userDetails));
    }

    @Override
    public R url_token_check(String url_path) {
        String token = Base64UrlUtils.decoding(url_path);
        // 这里使用 redis 的原因是 阻止用户多次访问重置密码的界面 在使用一次后就将其删除:如果 redis 中没有这个 token 就说明已经被使用过了
        // 删除 key 在重置密码的 UserInfoServiceImpl 部分 | 初始插入 key 在发送邮件的 ForgetPasswordMailServiceImpl 部分
        String email = JwtUtils.extractEmail(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Boolean check_res = JwtUtils.isTokenValid(token, userDetails) &&
                redisTemplate.opsForValue().get(RedisBaseKey.forget_password_token_base_name.getValue() + url_path) != null;
        return R.success("token查验成功", check_res);
    }
}