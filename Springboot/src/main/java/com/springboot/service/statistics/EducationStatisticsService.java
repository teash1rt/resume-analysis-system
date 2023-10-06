package com.springboot.service.statistics;

import com.springboot.entity.statistics.Education;

import java.util.List;

public interface EducationStatisticsService {
    void update_education_statistics(String education, Integer status);

    List<Education> get_education_statistics();
}
