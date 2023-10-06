package com.springboot.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.springboot.common.RedisBaseKey;
import com.springboot.config.exception.CustomException;
import com.springboot.entity.resume.Resume;
import com.springboot.entity.user.User;
import com.springboot.mapper.resume.ResumeMapper;
import com.springboot.mapper.user.UserMapper;
import com.springboot.service.mail.PermissionUpgradeMailService;
import com.springboot.service.resume.ResumeUploadService;
import com.springboot.service.user.ConfirmApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ConfirmApplicationServiceImpl implements ConfirmApplicationService {
    private final UserMapper userMapper;

    private final ResumeMapper resumeMapper;

    private final ResumeUploadService resumeUploadService;

    private final PermissionUpgradeMailService permissionUpgradeMailService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String confirm_permission_application(String email, String status, Model model) throws MessagingException {
        // 首先检查这个 token 是否被用过了 防止二次操作
        if (redisTemplate.opsForValue().get(RedisBaseKey.confirm_application_base_name.getValue() + email) == null) {
            model.addAttribute("status", "error");
            return "confirm_result.html";
        }

        // 不管审核结果如何首先删除该用户的申请中的状态 再把 token 删去表示已经用过了
        redisTemplate.delete(RedisBaseKey.waiting_permission_application_base_name.getValue() + email);
        redisTemplate.delete(RedisBaseKey.confirm_application_base_name.getValue() + email);

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("email", email);
        // 这里为什么要判空：从理论上来讲执行到这一步 email 一定是有所对应的不为空的
        // 但是有一种特殊的情况：用户提交了权限申请后，在超级管理员处理前就更改邮箱，此时 email 为空
        if (userMapper.selectOne(updateWrapper) == null) {
            throw new CustomException("用户不存在");
        }

        if (Objects.equals(status, "accept")) {
            // TODO 这里似乎应该即时更新用户信息 可以使用 redis 踢掉该用户的登录状态
            User user = userMapper.selectOne(updateWrapper);
            QueryWrapper<Resume> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", user.getUid());
            List<Resume> resumes = resumeMapper.selectList(queryWrapper);
            for (Resume resume : resumes) {
                resumeUploadService.del_upload_resume(resume.getRid());
            }
            updateWrapper.set("type", 1);
            userMapper.update(new User(), updateWrapper);
        }

        // 向用户发送申请结果
        permissionUpgradeMailService.send_response_email(email, status);
        model.addAttribute("status", status);
        return "confirm_result.html";
    }
}
