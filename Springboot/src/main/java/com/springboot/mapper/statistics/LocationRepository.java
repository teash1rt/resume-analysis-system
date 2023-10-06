package com.springboot.mapper.statistics;

import com.springboot.entity.statistics.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends MongoRepository<Location, String> {

    Location findByLocation(String location);

    List<Location> findByCountGreaterThan(Integer count);
}
