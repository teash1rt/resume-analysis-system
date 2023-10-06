package com.springboot.controller.user;

import com.springboot.common.R;
import com.springboot.service.user.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserInfoController {
    private final UserInfoService userinfoService;

    @PostMapping("/change-username/")
    public R change_username(@RequestBody Map<String, String> map) {
        String new_username = map.get("new_username");
        return userinfoService.change_username(new_username);
    }

    @PostMapping("/change-email/")
    public R change_email(@RequestBody Map<String, String> map) {
        String new_email = map.get("new_email");
        return userinfoService.change_email(new_email);
    }

    @PostMapping("/change-password/")
    public R change_password(@RequestBody Map<String, String> map) {
        String old_password = map.get("old_password");
        String new_password = map.get("new_password");
        return userinfoService.change_password(old_password, new_password);
    }

    // 用户登录 注册 更改用户信息后都会调用此接口
    @GetMapping("/get-info/")
    public R get_info(HttpServletResponse response) throws Exception {
        return userinfoService.get_userinfo(response);
    }

    @PostMapping("/forget-password/")
    public R forget_password(@RequestBody Map<String, String> map, HttpSession httpSession) throws Exception {
        String email = map.get("email");
        String verify_code = map.get("verify_code");
        return userinfoService.forget_password(email, verify_code, httpSession);
    }

    @PostMapping("/reset-password/")
    public R reset_password(@RequestBody Map<String, String> map) {
        String url_path = map.get("url_path");
        String new_password = map.get("new_password");
        return userinfoService.reset_password(url_path, new_password);
    }

    // 检查是否在审核权限中
    @PostMapping("/check-application-status/")
    @PreAuthorize("hasAnyAuthority('ROLE_0')")
    public R check_application_status(@RequestBody Map<String, String> map) {
        String email = map.get("email");
        return userinfoService.check_application_status(email);
    }

    // 申请权限时请求的邮箱验证码
    @PostMapping("/get-apply-verify-code/")
    @PreAuthorize("hasAnyAuthority('ROLE_0')")
    public R get_apply_verify_code(@RequestBody Map<String, String> map) throws MessagingException {
        String email = map.get("email");
        return userinfoService.get_apply_verify_code(email);
    }

    @PostMapping("/apply-permission/")
    @PreAuthorize("hasAnyAuthority('ROLE_0')")
    public R apply_permission(@RequestBody Map<String, String> map) throws MessagingException {
        String email = map.get("email");
        String purpose = map.get("purpose");
        String description = map.get("description");
        String verify_code = map.get("verify_code");
        return userinfoService.apply_permission(email, purpose, description, verify_code);
    }
}
