package com.springboot.controller.resume;

import com.springboot.common.R;
import com.springboot.service.resume.ResumeUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resume")
public class ResumeUploadController {
    private final ResumeUploadService resumeUploadService;

    @PostMapping("/upload/")
    @PreAuthorize("hasAnyAuthority('ROLE_0')")
    public R resume_upload(MultipartFile file, String summary_info, String detail_info, Float score, HttpServletRequest request) {
        return resumeUploadService.resume_upload(file, summary_info, detail_info, score, request);
    }

    @GetMapping("/get-upload-resumes/")
    @PreAuthorize("hasAnyAuthority('ROLE_0')")
    public R get_upload_resumes() {
        return resumeUploadService.get_upload_resumes();
    }

    @GetMapping("/check-before-upload/")
    @PreAuthorize("hasAnyAuthority('ROLE_0')")
    public R check_before_upload() {
        return resumeUploadService.check_before_upload();
    }

    @PostMapping("/del-upload-resume/")
    @PreAuthorize("hasAnyAuthority('ROLE_0')")
    public R del_upload_resume(@RequestBody Map<String, Integer> map) {
        Integer rid = map.get("rid");
        return resumeUploadService.del_upload_resume(rid);
    }
}
