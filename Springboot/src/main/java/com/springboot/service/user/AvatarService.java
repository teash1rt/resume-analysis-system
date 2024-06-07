package com.springboot.service.user;

import com.springboot.common.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface AvatarService {
    R setAvatar(MultipartFile file, HttpServletRequest request);

    R getAvatar();
}