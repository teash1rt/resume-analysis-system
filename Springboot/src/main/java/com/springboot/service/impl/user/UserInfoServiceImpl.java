package com.springboot.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.springboot.common.CookieManager;
import com.springboot.common.R;
import com.springboot.common.RedisBaseKey;
import com.springboot.entity.user.User;
import com.springboot.mapper.user.UserMapper;
import com.springboot.service.mail.ForgetPasswordMailService;
import com.springboot.service.mail.PermissionUpgradeMailService;
import com.springboot.service.user.UserInfoService;
import com.springboot.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {
    private final UserMapper userMapper;

    private final ForgetPasswordMailService forgetPasswordMailService;

    private final PermissionUpgradeMailService permissionUpgradeMailService;

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public R change_username(String new_username) {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        if (VerifyArgsUtils.is_empty(new_username)) {
            return R.error("新用户名不能为空");
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("uid", uid).set("username", new_username);
        userMapper.update(new User(), updateWrapper);
        return R.success("用户名更新成功");
    }

    @Override
    public R change_email(String new_email) {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        if (VerifyArgsUtils.is_empty(new_email)) {
            return R.error("新邮箱不能为空");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", new_email);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            return R.error("邮箱已被注册");
        }

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("uid", uid).set("email", new_email);
        userMapper.update(new User(), updateWrapper);
        return R.success("邮箱更新成功");
    }

    @Override
    public R change_password(String old_password, String new_password) {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        if (VerifyArgsUtils.is_empty(old_password, new_password)) {
            return R.error("密码不能为空");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid).eq("password", old_password);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return R.error("密码错误");
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("uid", uid).set("password", new_password);
        userMapper.update(new User(), updateWrapper);
        return R.success("密码更新成功");
    }

    @Override
    public R get_userinfo(HttpServletResponse response) throws Exception {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        User user = userMapper.selectById(uid);
        String key = RandomKeyUtils.getKey();
        String key_core = key.substring(2, 20);
        String username = user.getName();
        String email = user.getEmail();
        String type = String.valueOf(user.getType());
        String token = JwtUtils.generateToken(user);

        Integer expire_time = 48 * 60 * 60;
        CookieManager.setCookie("username", AesUtils.encrypt(username, key), expire_time, response);
        CookieManager.setCookie("email", AesUtils.encrypt(email, key), expire_time, response);
        CookieManager.setCookie("type", AesUtils.encrypt(type, key), expire_time, response);
        CookieManager.setCookie("token", AesUtils.encrypt(token, key), expire_time, response);
        CookieManager.setCookie("llave", key_core, expire_time, response);

        return R.success("查询用户信息成功");
    }

    @Override
    public R forget_password(String email, String verify_code, HttpSession httpSession) throws Exception {
        if (VerifyArgsUtils.is_empty(email)) {
            return R.error("注册邮箱不能为空");
        } else if (!VerifyCodeUtils.checkCode(verify_code, httpSession)) {
            return R.error("验证码错误");
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email", email);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user == null) {
            return R.error("邮箱不存在");
        }
        forgetPasswordMailService.send_email(email);
        return R.success("忘记密码邮件发送成功！请查看邮件");
    }

    @Override
    public R reset_password(String url_path, String new_password) {
        redisTemplate.delete(RedisBaseKey.forget_password_token_base_name.getValue() + url_path);

        String token = Base64UrlUtils.decoding(url_path);
        String email = JwtUtils.extractEmail(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (!JwtUtils.isTokenValid(token, userDetails)) {
            return R.error("token已失效");
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("email", email).set("password", passwordEncoder.encode(new_password));
        userMapper.update(new User(), updateWrapper);
        return R.success("重置密码成功");
    }

    @Override
    public R check_application_status(String email) {
        Boolean status = (Boolean) redisTemplate.opsForValue().get(RedisBaseKey.waiting_permission_application_base_name.getValue() + email);
        return R.success("查询申请状态成功", status);
    }

    @Override
    public R get_apply_verify_code(String email) throws MessagingException {
        permissionUpgradeMailService.send_verify_code(email);
        return R.success("申请权限邮箱验证码发送成功！请查看邮件");
    }

    @Override
    public R apply_permission(String email, String purpose, String description, String verify_code) throws MessagingException {
        // 拿到该邮箱对应的验证码
        String redis_verify_code_key = RedisBaseKey.permission_verify_code_base_name.getValue() + email;
        String redis_verify_code = (String) redisTemplate.opsForValue().get(redis_verify_code_key);
        if (VerifyArgsUtils.is_empty(email, purpose, description)) {
            return R.error("申请时各字段不能为空");
        } else if (redis_verify_code == null || !Objects.equals(redis_verify_code, verify_code)) {
            return R.error("验证码错误");
        }
        redisTemplate.delete(redis_verify_code_key);
        permissionUpgradeMailService.send_application_email(email, purpose, description);
        // 将该用户的状态变为申请中 避免重复申请
        redisTemplate.opsForValue().set(RedisBaseKey.waiting_permission_application_base_name.getValue() + email, true, 10, TimeUnit.DAYS);
        return R.success("申请成功！等待审核中");
    }
}
