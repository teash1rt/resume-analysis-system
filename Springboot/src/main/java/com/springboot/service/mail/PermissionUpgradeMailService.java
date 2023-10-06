package com.springboot.service.mail;

import javax.mail.MessagingException;

public interface PermissionUpgradeMailService {
    void send_verify_code(String email) throws MessagingException;

    // 向超级管理员发审核信息
    void send_application_email(String email, String purpose, String description) throws MessagingException;

    // 超级管理员审核后向用户反馈审核结果
    void send_response_email(String email, String status) throws MessagingException;
}
