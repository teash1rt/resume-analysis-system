package com.springboot.controller.resume;

import com.springboot.common.R;
import com.springboot.service.resume.ResumeInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resume")
public class ResumeInfoController {
    private final ResumeInfoService resumeInfoService;

    @GetMapping("/get-one-resume-info/{rid}/")
    @PreAuthorize("hasAnyAuthority('ROLE_1')")
    public R get_resume_info(@PathVariable Integer rid) {
        return resumeInfoService.get_one_resume_info(rid);
    }

    @GetMapping("/get-page-resumes-info/")
    @PreAuthorize("hasAnyAuthority('ROLE_1')")
    public R get_page_resumes_info(@RequestParam Integer page, @RequestParam Integer page_size, @RequestParam Integer sort_order) {
        return resumeInfoService.get_page_resumes_info(page, page_size, sort_order);
    }

    @GetMapping("/get-total-count/")
    @PreAuthorize("hasAnyAuthority('ROLE_1')")
    public R get_total_count() {
        return resumeInfoService.get_total_count();
    }
}
