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
    public R token_check(@RequestBody TokenCheckDTO tokenCheckDTO) {
        return tokencheckService.visitor_token_check(tokenCheckDTO.getToken());
    }

    @PostMapping("/url-token-check/")
    public R url_token_check(@RequestBody Map<String, String> map) {
        String url_path = map.get("url_path");
        return tokencheckService.url_token_check(url_path);
    }
}
