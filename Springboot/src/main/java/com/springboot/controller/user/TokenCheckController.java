package com.springboot.controller.user;

import com.springboot.common.R;
import com.springboot.dto.TokenCheckDTO;
import com.springboot.service.user.TokenCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class TokenCheckController {
    private final TokenCheckService tokencheckService;

    @PostMapping("/visitor-token-check/")
    public R visitorTokenCheck(@RequestBody TokenCheckDTO tokenCheckDTO) {
        return tokencheckService.visitorTokenCheck(tokenCheckDTO.getToken());
    }

    @PostMapping("/url-token-check/")
    public R urlTokenCheck(@RequestBody Map<String, String> map) {
        String url_path = map.get("url_path");
        return tokencheckService.urlTokenCheck(url_path);
    }
}
