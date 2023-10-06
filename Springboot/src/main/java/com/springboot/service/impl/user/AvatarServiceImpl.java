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
    public R set_avatar(MultipartFile file, HttpServletRequest request) {
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
        String old_name = file.getOriginalFilename();
        String new_name = null;
        if (old_name != null) {
            new_name = UUID.randomUUID() + old_name.substring(old_name.lastIndexOf("."));
        }
        try {
            file.transferTo(new File(folder, new_name));
            String route = new File(folder, new_name).getAbsolutePath();
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("uid", uid).set("avatar", route);

            // 在更新前删除
            String del_avatar_route = userMapper.selectById(uid).getAvatar();
            if (del_avatar_route != null) {
                File del_avatar = new File(del_avatar_route);
                del_avatar.delete();
            }
            userMapper.update(new User(), updateWrapper);
            // 更新 redis
            String redis_avatar_key = RedisBaseKey.avatar_base_name.getValue() + uid;
            redisTemplate.opsForValue().set(redis_avatar_key, Base64.getEncoder().encodeToString(file.getBytes()), 3, TimeUnit.DAYS);
            return R.success("头像更新成功");
        } catch (IOException e) {
            return R.error("头像更新失败");
        }
    }

    @Override
    public R get_avatar() {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        String filePath = userMapper.selectById(uid).getAvatar();
        if (filePath == null) {
            return R.success("获取默认头像成功", new byte[0]);
        }
        String redis_avatar_key = RedisBaseKey.avatar_base_name.getValue() + uid;
        String redis_avatar = redisTemplate.opsForValue().get(redis_avatar_key);
        if (redis_avatar != null) {
            return R.success("头像获取成功", redis_avatar);
        }
        try (FileInputStream inputStream = new FileInputStream(filePath);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] byteArr = outputStream.toByteArray();
            redisTemplate.opsForValue().set(redis_avatar_key, Base64.getEncoder().encodeToString(byteArr), 3, TimeUnit.DAYS);
            return R.success("头像获取成功", byteArr);
        } catch (IOException e) {
            return R.error("头像获取失败");
        }
    }
}
