package com.springboot.service.impl.resume;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.common.R;
import com.springboot.common.RedisBaseKey;
import com.springboot.entity.resume.Resume;
import com.springboot.entity.user.User;
import com.springboot.mapper.resume.ResumeMapper;
import com.springboot.service.resume.ResumeInfoService;
import com.springboot.vo.GetPageInfoVO;
import com.springboot.vo.PageInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ResumeInfoServiceImpl implements ResumeInfoService {
    private final ResumeMapper resumeMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public R getResumeInfo(Integer rid) {
        Object resumeInRedis = redisTemplate.opsForValue().get(RedisBaseKey.resume_base_name.getValue() + rid);
        if (resumeInRedis != null) {
            return R.success("简历信息查询成功", resumeInRedis);
        }

        QueryWrapper<Resume> resumeQueryWrapper = new QueryWrapper<>();
        resumeQueryWrapper.select("rid", "summary_info", "detail_info").eq("rid", rid);
        Resume resume = resumeMapper.selectOne(resumeQueryWrapper);
        redisTemplate.opsForValue().set(RedisBaseKey.resume_base_name.getValue() + rid, resume, 3, TimeUnit.DAYS);
        return R.success("简历信息查询成功", resume);
    }

    @Override
    public R getPageInfo(Integer page, Integer pageSize, Integer sortOrder) {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        PageHelper.startPage(page, pageSize);
        List<PageInfoVO> pages = resumeMapper.getPageInfo(uid, sortOrder);
        PageInfo<PageInfoVO> pageInfo = new PageInfo<>(pages);
        return R.success("分页数据查询成功", new GetPageInfoVO((int) pageInfo.getTotal(), pageInfo.getList()));
    }
}
