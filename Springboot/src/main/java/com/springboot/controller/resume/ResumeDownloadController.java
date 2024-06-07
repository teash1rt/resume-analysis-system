package com.springboot.controller.resume;

import com.springboot.common.R;
import com.springboot.dto.ResumeDownloadDTO;
import com.springboot.service.resume.ResumeDownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resume")
@RequiredArgsConstructor
public class ResumeDownloadController {
    private final ResumeDownloadService resumeDownloadService;

    @GetMapping("/download/")
    @PreAuthorize("hasAnyAuthority('ROLE_1')")
    public R resumeDownload(ResumeDownloadDTO resumeDownloadDTO) {
        return resumeDownloadService.resumeDownload(resumeDownloadDTO.getRid());
    }
}
