package com.springboot.service.resume;

import com.springboot.common.R;

public interface ResumeInfoService {
    R getResumeInfo(Integer rid);

    R getPageInfo(Integer page, Integer pageSize, Integer sortOrder);
}
