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
    public R get_resume_info(GetResumeInfoDTO getResumeInfoDTO) {
        return resumeInfoService.get_one_resume_info(getResumeInfoDTO.getRid());
    }

    @GetMapping("/get-page-resumes-info/")
    @PreAuthorize("hasAnyAuthority('ROLE_1')")
    public R get_page_resumes_info(GetPageResumesInfoDTO getPageResumesInfoDTO) {
        return resumeInfoService.get_page_resumes_info(
                getPageResumesInfoDTO.getPage(),
                getPageResumesInfoDTO.getPageSize(),
                getPageResumesInfoDTO.getSortOrder()
        );
    }

    @GetMapping("/get-total-count/")
    @PreAuthorize("hasAnyAuthority('ROLE_1')")
    public R get_total_count() {
        return resumeInfoService.get_total_count();
    }
}
