package com.springboot.service.resume;

import com.springboot.common.R;

public interface ResumeInfoService {
    R get_one_resume_info(Integer rid);

    R get_page_resumes_info(int page, int page_size, int sort_order);

    R get_total_count();
}
