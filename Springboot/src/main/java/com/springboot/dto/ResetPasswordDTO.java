package com.springboot.dto;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String urlPath;
    private String newPassword;
}
