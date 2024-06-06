package com.springboot.controller.user;

import com.springboot.common.R;
import com.springboot.dto.*;
import com.springboot.service.user.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserInfoController {
    private final UserInfoService userinfoService;

    @PostMapping("/change-username/")
    public R change_username(@RequestBody ChangeUsernameDTO changeUsernameDTO) {
        return userinfoService.change_username(changeUsernameDTO.getNewUsername());
    }

    @PostMapping("/change-email/")
    public R change_email(@RequestBody ChangeEmailDTO changeEmailDTO) {
        return userinfoService.change_email(changeEmailDTO.getNewEmail());
    }

    @PostMapping("/change-password/")
    public R change_password(@RequestBody ChangePasswordDTO changePasswordDTO) {
        return userinfoService.change_password(
                changePasswordDTO.getOldPassword(),
                changePasswordDTO.getNewPassword()
        );
    }

    // 用户登录 注册 更改用户信息后都会调用此接口
    @GetMapping("/get-info/")
    public R get_info(HttpServletResponse response) throws Exception {
        return userinfoService.get_userinfo(response);
    }

    @PostMapping("/forget-password/")
    public R forget_password(@RequestBody ForgetPasswordDTO forgetPasswordDTO, HttpSession httpSession) throws Exception {
        return userinfoService.forget_password(
                forgetPasswordDTO.getEmail(),
                forgetPasswordDTO.getVerifyCode(),
                httpSession
        );
    }

    @PostMapping("/reset-password/")
    public R reset_password(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        return userinfoService.reset_password(resetPasswordDTO.getUrlPath(), resetPasswordDTO.getNewPassword());
    }

    // 检查是否在审核权限中
    @PostMapping("/check-application-status/")
    @PreAuthorize("hasAnyAuthority('ROLE_0')")
    public R check_application_status() {
        return userinfoService.check_application_status();
    }

    // 申请权限时请求的邮箱验证码
    @GetMapping("/get-apply-verify-code/")
    @PreAuthorize("hasAnyAuthority('ROLE_0')")
    public R get_apply_verify_code() throws MessagingException {
        return userinfoService.get_apply_verify_code();
    }

    @PostMapping("/apply-permission/")
    @PreAuthorize("hasAnyAuthority('ROLE_0')")
    public R apply_permission(@RequestBody ApplyPermissionDTO applyPermissionDTO) throws MessagingException {
        return userinfoService.apply_permission(
                applyPermissionDTO.getEmail(),
                applyPermissionDTO.getPurpose(),
                applyPermissionDTO.getDescription(),
                applyPermissionDTO.getVerifyCode()
        );
    }
}
