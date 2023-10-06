package com.springboot.service.impl.resume;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.common.R;
import com.springboot.common.RedisBaseKey;
import com.springboot.entity.resume.Resume;
import com.springboot.entity.resume.ResumeFavorite;
import com.springboot.entity.user.User;
import com.springboot.mapper.resume.ResumeFavoriteMapper;
import com.springboot.mapper.resume.ResumeMapper;
import com.springboot.mapper.user.UserMapper;
import com.springboot.service.resume.ResumeInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ResumeInfoServiceImpl implements ResumeInfoService {
    private final ResumeMapper resumeMapper;

    private final UserMapper userMapper;

    private final ResumeFavoriteMapper resumeFavoriteMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public R get_one_resume_info(Integer rid) {
        Object redis_resume = redisTemplate.opsForValue().get(RedisBaseKey.resume_base_name.getValue() + rid);
        if (redis_resume != null) {
            return R.success("简历信息查询成功", redis_resume);
        }

        QueryWrapper<Resume> resumeQueryWrapper = new QueryWrapper<>();
        resumeQueryWrapper.eq("rid", rid).select("rid", "summary_info", "detail_info");
        Resume resume = resumeMapper.selectOne(resumeQueryWrapper);
        redisTemplate.opsForValue().set(RedisBaseKey.resume_base_name.getValue() + rid, resume, 3, TimeUnit.DAYS);
        return R.success("简历信息查询成功", resume);
    }

    @Override
    public R get_page_resumes_info(int page, int page_size, int sort_order) {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        QueryWrapper<Resume> resumeQueryWrapper = new QueryWrapper<>();
        resumeQueryWrapper.select("rid", "uid", "summary_info", "create_time");

        if (sort_order == 1) {
            resumeQueryWrapper.orderByDesc("rid");
        } else if (sort_order == 2) {
            resumeQueryWrapper.orderByDesc("score");
        }
        // 分页
        PageHelper.startPage(page, page_size);
        List<Resume> resumes = resumeMapper.selectList(resumeQueryWrapper);
        PageInfo<Resume> resumePageInfo = new PageInfo<>(resumes);

        // 找到这个简历的上传者
        List<Resume> resumeList = resumePageInfo.getList();
        List<Map<String, Object>> resumeInfo = new ArrayList<>();

        QueryWrapper<ResumeFavorite> resumeFavoriteQueryWrapper = new QueryWrapper<>();
        resumeFavoriteQueryWrapper.eq("uid", uid).select("rid");

        // 找到 uid 收藏的简历
        List<ResumeFavorite> resumeFavorites = resumeFavoriteMapper.selectList(resumeFavoriteQueryWrapper);
        resumeList.forEach(e -> {
            Map<String, Object> info_map = new HashMap<>();
            String username = userMapper.selectById(e.getUid()).getName();
            info_map.put("rid", e.getRid());
            info_map.put("username", username);
            info_map.put("summaryInfo", e.getSummaryInfo());
            info_map.put("createTime", e.getCreateTime());

            boolean isResumeFavorite = false;
            // 找到收藏的
            for (ResumeFavorite r : resumeFavorites) {
                if (Objects.equals(r.getRid(), e.getRid())) {
                    isResumeFavorite = true;
                    break;
                }
            }
            if (isResumeFavorite) {
                info_map.put("favorite", true);
            } else {
                info_map.put("favorite", false);
            }
            resumeInfo.add(info_map);
        });
        return R.success("分页数据查询成功", resumeInfo);
    }

    @Override
    public R get_total_count() {
        Long count = resumeMapper.selectCount(null);
        return R.success("简历总数量查询成功", count);
    }
}
