package com.springboot.service.user;

import com.springboot.common.R;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface UserInfoService {
    R change_username(String new_username);

    R change_email(String new_email);

    R change_password(String old_password, String new_password);

    R get_userinfo(HttpServletResponse response) throws Exception;

    R forget_password(String email, String verify_code, HttpSession httpSession) throws Exception;

    R reset_password(String url_path, String new_password);

    R check_application_status(String email);// 检查是否是正在审核中的状态 避免重复提交

    R get_apply_verify_code(String email) throws MessagingException;// get_apply_verify_code 为申请者申请前获得验证码

    R apply_permission(String email, String purpose, String description, String verify_code) throws MessagingException;// apply_permission 为向超级管理员发送该申请者的相关信息
}
