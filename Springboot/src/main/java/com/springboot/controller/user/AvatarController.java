package com.springboot.controller.user;

import com.springboot.common.R;
import com.springboot.service.user.AvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AvatarController {
    private final AvatarService avatarService;

    @PostMapping("/set-avatar/")
    public R setAvatar(MultipartFile file, HttpServletRequest request) {
        return avatarService.setAvatar(file, request);
    }

    @GetMapping("/get-avatar/")
    public R getAvatar() {
        return avatarService.getAvatar();
    }
}
