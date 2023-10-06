package com.springboot.service.impl.mail;

import com.springboot.common.RedisBaseKey;
import com.springboot.service.mail.PermissionUpgradeMailService;
import com.springboot.utils.RandomKeyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PermissionUpgradeMailServiceImpl implements PermissionUpgradeMailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final Environment environment;
    // 获取yml配置的发送者邮箱
    @Value("${spring.mail.username}")
    private String mainUserName;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Async
    @Override
    public void send_verify_code(String email) throws MessagingException {
        // 这里使用 redis 暂存验证码比较方便
        String verify_code = RandomKeyUtils.randomString(6);
        redisTemplate.opsForValue().set(RedisBaseKey.permission_verify_code_base_name.getValue() + email, verify_code, 5, TimeUnit.MINUTES);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setSubject("【邮箱验证码】");
        mimeMessageHelper.setFrom("简历解析系统" + '<' + mainUserName + '>');
        mimeMessageHelper.setTo(email);
        Context ctx = new Context();
        ctx.setVariable("verify_code", verify_code);
        String emailText = templateEngine.process("./get_verify_code.html", ctx);
        mimeMessageHelper.setText(emailText, true);
        javaMailSender.send(mimeMessage);
    }

    @Async
    @Override
    public void send_application_email(String email, String purpose, String description) throws MessagingException {
        String url;
        if (Objects.equals(environment.getActiveProfiles()[0], "dev")) {
            url = "http://localhost:4080/user/confirm-permission-application/";
        } else {
            url = "http://117.50.181.49:4080/user/confirm-permission-application/";
        }

        // 向超级管理员发送消息 这里假定只有唯一超级管理员 如果今后有多个 可在数据库中寻找所有 type = 2 的用户
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setSubject("【收到新的申请】");
        mimeMessageHelper.setFrom("简历解析系统" + '<' + mainUserName + '>');
        mimeMessageHelper.setTo("1223696764@qq.com");
        Context ctx = new Context();
        ctx.setVariable("email", email);
        ctx.setVariable("purpose", purpose);
        ctx.setVariable("description", description);

        // 生成一个使用一次就过期的 token 防止在同一个邮件中多次更改权限 这里的实现逻辑与忘记密码的实现流程基本相同
        String token = RandomKeyUtils.randomString(10);
        redisTemplate.opsForValue().set(RedisBaseKey.confirm_application_base_name.getValue() + email, token, 10, TimeUnit.DAYS);

        ctx.setVariable("refuse_url", url + email + "/refuse/" + token + "/");
        ctx.setVariable("accept_url", url + email + "/accept/" + token + "/");
        String emailText = templateEngine.process("./confirm_application.html", ctx);
        mimeMessageHelper.setText(emailText, true);
        javaMailSender.send(mimeMessage);
    }

    @Async
    @Override
    public void send_response_email(String email, String status) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setSubject("【申请审核结果】");
        mimeMessageHelper.setFrom("简历解析系统" + '<' + mainUserName + '>');
        mimeMessageHelper.setTo(email);
        Context ctx = new Context();
        ctx.setVariable("status", status);
        String emailText = templateEngine.process("./response_application.html", ctx);
        mimeMessageHelper.setText(emailText, true);
        javaMailSender.send(mimeMessage);
    }
}
