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
    public R changeUsername(@RequestBody ChangeUsernameDTO changeUsernameDTO) {
        return userinfoService.changeUsername(changeUsernameDTO.getNewUsername());
    }

    @PostMapping("/change-email/")
    public R changeEmail(@RequestBody ChangeEmailDTO changeEmailDTO) {
        return userinfoService.changeEmail(changeEmailDTO.getNewEmail());
    }

    @PostMapping("/change-password/")
    public R changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        return userinfoService.changePassword(
                changePasswordDTO.getOldPassword(),
                changePasswordDTO.getNewPassword()
        );
    }

    // 用户登录 注册 更改用户信息后都会调用此接口
    @GetMapping("/get-info/")
    public R getUserinfo(HttpServletResponse response) throws Exception {
        return userinfoService.getUserinfo(response);
    }

    @PostMapping("/forget-password/")
    public R forgetPassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO, HttpSession httpSession) throws Exception {
        return userinfoService.forgetPassword(
                forgetPasswordDTO.getEmail(),
                forgetPasswordDTO.getVerifyCode(),
                httpSession
        );
    }

    @PostMapping("/reset-password/")
    public R resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        return userinfoService.resetPassword(resetPasswordDTO.getUrlPath(), resetPasswordDTO.getNewPassword());
    }

    // 检查是否在审核权限中
    @PostMapping("/check-application-status/")
    @PreAuthorize("hasAnyAuthority('ROLE_0')")
    public R checkApplicationStatus() {
        return userinfoService.checkApplicationStatus();
    }

    // 申请权限时请求的邮箱验证码
    @GetMapping("/get-apply-verify-code/")
    @PreAuthorize("hasAnyAuthority('ROLE_0')")
    public R getApplyVerifyCode() throws MessagingException {
        return userinfoService.getApplyVerifyCode();
    }

    @PostMapping("/apply-permission/")
    @PreAuthorize("hasAnyAuthority('ROLE_0')")
    public R applyPermission(@RequestBody ApplyPermissionDTO applyPermissionDTO) throws MessagingException {
        return userinfoService.applyPermission(
                applyPermissionDTO.getEmail(),
                applyPermissionDTO.getPurpose(),
                applyPermissionDTO.getDescription(),
                applyPermissionDTO.getVerifyCode()
        );
    }
}
