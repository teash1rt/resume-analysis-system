package com.springboot.service.statistics;

import com.springboot.common.R;

public interface StatisticsInfoService {
    R getStatisticsInfo();

    void update_statistics_info(String summary_info, Integer status, Integer rid);
}
