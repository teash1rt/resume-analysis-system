package com.springboot.controller.user;

import com.springboot.service.user.ConfirmApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class ConfirmApplicationController {
    private final ConfirmApplicationService confirmApplicationService;

    @GetMapping("/confirm-permission-application/{email}/{status}/{token}/")
    public String confirm_permission_application(@PathVariable String email, @PathVariable String status, Model model) throws MessagingException {
        // 此处返回的是 html 页面
        return confirmApplicationService.confirm_permission_application(email, status, model);
    }
}
