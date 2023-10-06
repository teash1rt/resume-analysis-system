package com.springboot.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalculateUtils {
    // TODO 30天过期
    private static final Double[] days_score_in_month = {10.102, 10.047, 9.977, 9.89, 9.779, 9.641, 9.469, 9.258, 9.0, 8.691, 8.324, 7.898, 7.412, 6.873, 6.29, 5.678, 5.054, 4.437, 3.847, 3.297, 2.8, 2.361, 1.982, 1.66, 1.392, 1.172, 0.992, 0.848, 0.732, 0.639, 0.566, 0.509};

    // 简历热度的计算方法
    // f(days, favorite_count, resume_score) = p(days) + q(favorite_count) + r(resume_score)
    // p(days) = 1 / (0.1 + 0.3 * e^(days / 4 - 5)) + 0.3
    // q(favorite_count) = 10 * Math.log10(favorite_count + 1)
    // r(resume_score) = sqrt(resume_score)
    public static Double getFavoriteScore(int days, Long favorite_count, Float resume_score) {
        // TODO 此处应该添加mysql脚本 自动删除30天以上的简历
        double days_score = days <= 30 ? days_score_in_month[days] : 0.3;
        return Double.valueOf(String.format("%.1f", days_score + 10 * Math.log10(favorite_count + 1) + Math.sqrt(resume_score)));
    }
}

