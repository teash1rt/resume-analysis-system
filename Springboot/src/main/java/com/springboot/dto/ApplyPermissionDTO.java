package com.springboot.dto;

import lombok.Data;

@Data
public class ApplyPermissionDTO {
    private String email;
    private String purpose;
    private String description;
    private String verifyCode;
}
