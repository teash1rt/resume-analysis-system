package com.springboot.controller.resume;

import com.springboot.common.R;
import com.springboot.dto.GetPageResumesInfoDTO;
import com.springboot.dto.GetResumeInfoDTO;
import com.springboot.service.resume.ResumeInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resume")
public class ResumeInfoController {
    private final ResumeInfoService resumeInfoService;

    @GetMapping("/get-resume-info/")
    @PreAuthorize("hasAnyAuthority('ROLE_1')")
    public R getResumeInfo(GetResumeInfoDTO getResumeInfoDTO) {
        return resumeInfoService.getResumeInfo(getResumeInfoDTO.getRid());
    }

    @GetMapping("/get-page-info/")
    @PreAuthorize("hasAnyAuthority('ROLE_1')")
    public R getPageInfo(GetPageResumesInfoDTO getPageResumesInfoDTO) {
        return resumeInfoService.getPageInfo(
                getPageResumesInfoDTO.getPage(),
                getPageResumesInfoDTO.getPageSize(),
                getPageResumesInfoDTO.getSortOrder()
        );
    }
}
