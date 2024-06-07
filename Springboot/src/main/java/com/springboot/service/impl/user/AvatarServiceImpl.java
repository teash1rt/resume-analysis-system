package com.springboot.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.springboot.common.R;
import com.springboot.common.RedisBaseKey;
import com.springboot.entity.user.User;
import com.springboot.mapper.user.UserMapper;
import com.springboot.service.user.AvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {
    private final UserMapper userMapper;
    private final Environment environment;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public R setAvatar(MultipartFile file, HttpServletRequest request) {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        String realPath;
        if (Objects.equals(environment.getActiveProfiles()[0], "dev")) {
            realPath = request.getSession().getServletContext().getRealPath("/user_avatar/");
        } else {
            realPath = "/home/ubuntu/static/user_avatar/";
        }
        File folder = new File(realPath);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = null;
        if (oldName != null) {
            newName = UUID.randomUUID() + oldName.substring(oldName.lastIndexOf("."));
        }
        try {
            file.transferTo(new File(folder, newName));
            String route = new File(folder, newName).getAbsolutePath();
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("uid", uid).set("avatar", route);

            // 在更新前删除
            String path = userMapper.selectById(uid).getAvatar();
            if (path != null) {
                File avatar = new File(path);
                boolean success = avatar.delete();
                if (!success) {
                    return R.error("头像更新失败");
                }
            }
            userMapper.update(new User(), updateWrapper);
            // 更新 redis
            String avatarKey = RedisBaseKey.avatar_base_name.getValue() + uid;
            redisTemplate.opsForValue().set(avatarKey, Base64.getEncoder().encodeToString(file.getBytes()), 3, TimeUnit.DAYS);
            return R.success("头像更新成功");
        } catch (IOException e) {
            return R.error("头像更新失败");
        }
    }

    @Override
    public R getAvatar() {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        String filePath = userMapper.selectById(uid).getAvatar();
        if (filePath == null) {
            return R.success("获取默认头像成功", new byte[0]);
        }
        String avatarKey = RedisBaseKey.avatar_base_name.getValue() + uid;
        String avatar = redisTemplate.opsForValue().get(avatarKey);
        if (avatar != null) {
            return R.success("头像获取成功", avatar);
        }
        try (FileInputStream inputStream = new FileInputStream(filePath);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] byteArr = outputStream.toByteArray();
            redisTemplate.opsForValue().set(avatarKey, Base64.getEncoder().encodeToString(byteArr), 3, TimeUnit.DAYS);
            return R.success("头像获取成功", byteArr);
        } catch (IOException e) {
            return R.error("头像获取失败");
        }
    }
}
