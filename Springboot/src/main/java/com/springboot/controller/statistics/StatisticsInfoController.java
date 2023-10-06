package com.springboot.controller.statistics;

import com.springboot.common.R;
import com.springboot.service.statistics.StatisticsInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class StatisticsInfoController {
    private final StatisticsInfoService statisticsInfoService;

    @GetMapping("/get-statistics-info/")
    @PreAuthorize("hasAnyAuthority('ROLE_1')")
    public R get_statistics_info() {
        return statisticsInfoService.get_statistics_info();
    }
}
