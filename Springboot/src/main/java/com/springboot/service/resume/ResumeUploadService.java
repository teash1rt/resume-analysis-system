package com.springboot.service.resume;

import com.springboot.common.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface ResumeUploadService {
    R resume_upload(MultipartFile file, String summary_info, String detail_info, Float score, HttpServletRequest request);

    R get_upload_resumes();

    R check_before_upload();

    R del_upload_resume(Integer rid);
}
