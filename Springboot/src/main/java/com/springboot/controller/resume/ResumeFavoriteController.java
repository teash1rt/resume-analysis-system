package com.springboot.controller.resume;

import com.springboot.common.R;
import com.springboot.service.resume.ResumeFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resume")
public class ResumeFavoriteController {
    private final ResumeFavoriteService resumeFavoriteService;

    @PostMapping("/add-favorite/")
    @PreAuthorize("hasAnyAuthority('ROLE_1')")
    public R add_favorite(@RequestBody Map<String, Integer> map) {
        Integer rid = map.get("rid");
        return resumeFavoriteService.add_favorite(rid);
    }

    @PostMapping("/cancel-favorite/")
    @PreAuthorize("hasAnyAuthority('ROLE_1')")
    public R cancel_favorite(@RequestBody Map<String, Integer> map) {
        Integer rid = map.get("rid");
        return resumeFavoriteService.cancel_favorite(rid);
    }

    @GetMapping("/get-favorite/")
    @PreAuthorize("hasAnyAuthority('ROLE_1')")
    public R get_favorite() {
        return resumeFavoriteService.get_favorite();
    }
}
