package com.springboot.service.user;

import com.springboot.common.R;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface UserInfoService {
    R changeUsername(String new_username);

    R changeEmail(String new_email);

    R changePassword(String old_password, String new_password);

    R getUserinfo(HttpServletResponse response) throws Exception;

    R forgetPassword(String email, String verify_code, HttpSession httpSession) throws Exception;

    R resetPassword(String url_path, String new_password);

    R checkApplicationStatus();// 检查是否是正在审核中的状态 避免重复提交

    R getApplyVerifyCode() throws MessagingException;// getApplyVerifyCode 为申请者申请前获得验证码

    R applyPermission(String email, String purpose, String description, String verify_code) throws MessagingException;// applyPermission 为向超级管理员发送该申请者的相关信息
}
