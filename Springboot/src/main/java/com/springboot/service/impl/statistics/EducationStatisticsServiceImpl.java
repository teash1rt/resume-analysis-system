package com.springboot.service.impl.statistics;

import com.springboot.entity.statistics.Education;
import com.springboot.mapper.statistics.EducationRepository;
import com.springboot.service.statistics.EducationStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationStatisticsServiceImpl implements EducationStatisticsService {
    private final EducationRepository educationRepository;

    @Override
    public void update_education_statistics(String education, Integer status) {
        Education edu = educationRepository.findByEducation(education);
        if (edu != null) {
            edu.setCount(edu.getCount() + status);
            educationRepository.save(edu);
        }
    }

    @Override
    public List<Education> get_education_statistics() {
        return educationRepository.findByCountGreaterThan(0);
    }
}
