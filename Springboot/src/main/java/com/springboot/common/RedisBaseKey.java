package com.springboot.common;


import lombok.Getter;

@Getter
public enum RedisBaseKey {
    avatar_base_name("avatarBy"),   // 用户头像数据
    resume_base_name("resumeBy"),   // 单个简历详情
    waiting_permission_application_base_name("waitingPermissionApplicationBy"), // 记录该用户是否为申请中的状态
    permission_verify_code_base_name("permissionVerifyCodeBy"), // 申请权限时向用户发送的验证码
    statistics_info_name("statistics_info"),    // 数据可视化时的数据
    forget_password_token_base_name("forgetPasswordTokenBy"),   // 忘记密码时候生成的 token 作为 url 路径的一部分 防止多次更改密码
    confirm_application_base_name("confirmApplicationBy");  // 超级管理员点击得到审核结果的 url 中的 token 防止多次操作同一个审核

    private final String value;

    RedisBaseKey(String value) {
        this.value = value;
    }

}