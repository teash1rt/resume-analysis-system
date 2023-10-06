package com.springboot.service.impl.statistics;

import com.springboot.entity.statistics.Location;
import com.springboot.mapper.statistics.LocationRepository;
import com.springboot.service.statistics.LocationStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationStatisticsServiceImpl implements LocationStatisticsService {

    private final LocationRepository locationRepository;

    @Override
    public void update_location_statistics(String location, Integer status) {
        Location loc = locationRepository.findByLocation(location);
        if (loc != null) {
            loc.setCount(loc.getCount() + status);
            locationRepository.save(loc);
        }
    }

    @Override
    public List<Location> get_location_statistics() {
        return locationRepository.findByCountGreaterThan(0);
    }
}
