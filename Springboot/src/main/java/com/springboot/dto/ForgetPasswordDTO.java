package com.springboot.dto;

import lombok.Data;

@Data
public class ForgetPasswordDTO {
    private String email;
    private String verifyCode;
}
