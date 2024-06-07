package com.springboot.service.resume;

import com.springboot.common.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface ResumeUploadService {
    R resumeUpload(MultipartFile file, String summary_info, String detail_info, Float score, HttpServletRequest request);

    R getUploadResumes();

    R delUploadResume(Integer rid);
}
