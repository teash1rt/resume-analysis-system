package com.springboot.service.mail;

public interface ForgetPasswordMailService {
    void send_email(String email) throws Exception;
}
