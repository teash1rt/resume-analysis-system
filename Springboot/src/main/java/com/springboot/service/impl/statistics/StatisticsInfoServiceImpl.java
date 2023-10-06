package com.springboot.service.impl.statistics;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.springboot.common.R;
import com.springboot.common.RedisBaseKey;
import com.springboot.service.statistics.EducationStatisticsService;
import com.springboot.service.statistics.ExperienceStatisticsService;
import com.springboot.service.statistics.LocationStatisticsService;
import com.springboot.service.statistics.StatisticsInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class StatisticsInfoServiceImpl implements StatisticsInfoService {
    private final LocationStatisticsService locationStatisticsService;

    private final EducationStatisticsService educationStatisticsService;

    private final ExperienceStatisticsService experienceStatisticsService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public R get_statistics_info() {
        Object redis_statistics_info = redisTemplate.opsForValue().get(RedisBaseKey.statistics_info_name.getValue());
        if (redis_statistics_info != null) {
            return R.success("查询统计信息成功", redis_statistics_info);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("location_statistics", locationStatisticsService.get_location_statistics());
        map.put("education_statistics", educationStatisticsService.get_education_statistics());
        map.put("experience_statistics", experienceStatisticsService.get_experience_statistics());

        redisTemplate.opsForValue().set(RedisBaseKey.statistics_info_name.getValue(), map, 3, TimeUnit.DAYS);
        return R.success("朴素查询统计信息成功", map);
    }

    @Async
    @Override
    public void update_statistics_info(String summary_info, Integer status, Integer rid) {
        redisTemplate.delete(RedisBaseKey.statistics_info_name.getValue());

        JSONObject info = JSON.parseObject(summary_info);
        JSONObject tag = info.getJSONObject("tag");

        String loc = tag.getString("loc_tag");
        locationStatisticsService.update_location_statistics(loc, status);

        Integer total_company_count = tag.getJSONArray("experience_tag").size();
        Integer total_work_time = tag.getInteger("total_work_time");
        experienceStatisticsService.update_experience_statistics(total_company_count, total_work_time, status, rid);

        String edu = info.getJSONObject("basic_data").getString("edu");
        educationStatisticsService.update_education_statistics(edu, status);
    }
}
