package com.springboot.service.user;

import org.springframework.ui.Model;

import javax.mail.MessagingException;

public interface ConfirmApplicationService {
    String confirm_permission_application(String email, String status, Model model) throws MessagingException;
}
