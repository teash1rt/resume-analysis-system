package com.springboot.controller.resume;

import com.springboot.common.R;
import com.springboot.dto.AddFavoriteDTO;
import com.springboot.dto.CancelFavoriteDTO;
import com.springboot.service.resume.ResumeFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resume")
public class ResumeFavoriteController {
    private final ResumeFavoriteService resumeFavoriteService;

    @PostMapping("/add-favorite/")
    @PreAuthorize("hasAnyAuthority('ROLE_1')")
    public R add_favorite(@RequestBody AddFavoriteDTO addFavoriteDTO) {
        return resumeFavoriteService.add_favorite(addFavoriteDTO.getRid());
    }

    @PostMapping("/cancel-favorite/")
    @PreAuthorize("hasAnyAuthority('ROLE_1')")
    public R cancel_favorite(@RequestBody CancelFavoriteDTO cancelFavoriteDTO) {
        return resumeFavoriteService.cancel_favorite(cancelFavoriteDTO.getRid());
    }

    @GetMapping("/get-favorite/")
    @PreAuthorize("hasAnyAuthority('ROLE_1')")
    public R get_favorite() {
        return resumeFavoriteService.get_favorite();
    }
}
