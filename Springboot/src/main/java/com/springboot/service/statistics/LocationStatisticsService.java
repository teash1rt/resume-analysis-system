package com.springboot.service.statistics;

import com.springboot.entity.statistics.Location;

import java.util.List;

public interface LocationStatisticsService {
    void update_location_statistics(String location, Integer status);

    List<Location> get_location_statistics();
}
