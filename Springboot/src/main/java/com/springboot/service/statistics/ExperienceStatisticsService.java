package com.springboot.service.statistics;

import java.util.ArrayList;
import java.util.Map;

public interface ExperienceStatisticsService {
    void update_experience_statistics(Integer total_company_count, Integer total_work_time, Integer status, Integer rid);

    Map<String, ArrayList<Integer>> get_experience_statistics();
}
