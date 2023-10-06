package com.springboot.mapper.statistics;

import com.springboot.entity.statistics.Education;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends MongoRepository<Education, String> {

    Education findByEducation(String education);

    List<Education> findByCountGreaterThan(Integer count);
}
