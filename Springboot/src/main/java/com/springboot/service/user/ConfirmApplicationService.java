package com.springboot.service.user;

import org.springframework.ui.Model;

import javax.mail.MessagingException;

public interface ConfirmApplicationService {
    String confirmPermissionApplication(String email, String status, Model model) throws MessagingException;
}
