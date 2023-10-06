package com.springboot.service.impl.mail;

import com.springboot.common.RedisBaseKey;
import com.springboot.entity.user.User;
import com.springboot.service.mail.ForgetPasswordMailService;
import com.springboot.utils.Base64UrlUtils;
import com.springboot.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ForgetPasswordMailServiceImpl implements ForgetPasswordMailService {
    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;
    // 获取yml配置的发送者邮箱
    @Value("${spring.mail.username}")
    private String mainUserName;

    @Value("${address}")
    private String address;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Async
    @Override
    public void send_email(String email) throws Exception {
        String token = JwtUtils.generateToken(new User().setEmail(email));
        String url_path = Base64UrlUtils.encoding(token);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setSubject("【重置密码】");
        mimeMessageHelper.setFrom("简历解析系统" + '<' + mainUserName + '>');
        mimeMessageHelper.setTo(email);

        String url = address + "reset-password/" + url_path + "/";

        Context ctx = new Context();
        ctx.setVariable("href", url);
        String emailText = templateEngine.process("./to_reset_password.html", ctx);
        mimeMessageHelper.setText(emailText, true);

        // 这里使用 redis 的原因见 TokenCheckServiceImpl
        redisTemplate.opsForValue().set(RedisBaseKey.forget_password_token_base_name.getValue() + url_path, true, 10, TimeUnit.MINUTES);
        javaMailSender.send(mimeMessage);
    }
}
