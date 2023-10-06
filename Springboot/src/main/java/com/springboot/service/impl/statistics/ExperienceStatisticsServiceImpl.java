package com.springboot.service.impl.statistics;

import com.springboot.entity.statistics.Experience;
import com.springboot.mapper.statistics.ExperienceRepository;
import com.springboot.service.statistics.ExperienceStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ExperienceStatisticsServiceImpl implements ExperienceStatisticsService {
    private final ExperienceRepository experienceRepository;

    @Override
    public void update_experience_statistics(Integer total_company_count, Integer total_work_time, Integer status, Integer rid) {
        String id = experienceRepository.findAllBy().get(0).getId();
        Experience experience = experienceRepository.findById(id).get();
        ArrayList<Integer> rids = experience.getRids();
        ArrayList<Integer> arr_company_count = experience.getTotal_company_count();
        ArrayList<Integer> arr_work_time = experience.getTotal_work_time();
        if (status == 1) {
            rids.add(rid);
            arr_company_count.add(total_company_count);
            arr_work_time.add(total_work_time);
        } else if (status == -1) {
            // TODO 此处应优化 rids 存储逻辑为优先队列 然后使用二分进行查找
            for (int i = 0; i < rids.size(); i++) {
                if (Objects.equals(rids.get(i), rid)) {
                    rids.remove(i);
                    arr_company_count.remove(i);
                    arr_work_time.remove(i);
                    break;
                }
            }
        }
        experience.setTotal_company_count(rids);
        experience.setTotal_company_count(arr_company_count);
        experience.setTotal_work_time(arr_work_time);
        experienceRepository.save(experience);
    }

    @Override
    public Map<String, ArrayList<Integer>> get_experience_statistics() {
        Map<String, ArrayList<Integer>> map = new HashMap<>();
        List<Experience> exp = experienceRepository.findAllBy();
        map.put("total_company_count", exp.get(0).getTotal_company_count());
        map.put("total_work_time", exp.get(0).getTotal_work_time());
        return map;
    }
}
