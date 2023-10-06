package com.springboot.service.statistics;

import com.springboot.common.R;

public interface StatisticsInfoService {
    R get_statistics_info();

    void update_statistics_info(String summary_info, Integer status, Integer rid);
}
