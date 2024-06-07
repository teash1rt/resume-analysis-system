package com.springboot.controller.resume;

import com.springboot.common.R;
import com.springboot.dto.FavoriteResumeDTO;
import com.springboot.service.resume.ResumeFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resume")
public class ResumeFavoriteController {
    private final ResumeFavoriteService resumeFavoriteService;

    @PostMapping("/favorite-resume/")
    @PreAuthorize("hasAnyAuthority('ROLE_1')")
    public R favoriteResume(@RequestBody FavoriteResumeDTO favoriteResumeDTO) {
        return resumeFavoriteService.favoriteResume(
                favoriteResumeDTO.getRid(),
                favoriteResumeDTO.getIsFavorite()
        );
    }

    @GetMapping("/get-favorite/")
    @PreAuthorize("hasAnyAuthority('ROLE_1')")
    public R getFavoriteResumes() {
        return resumeFavoriteService.getFavoriteResumes();
    }
}
