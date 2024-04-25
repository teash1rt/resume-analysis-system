package com.springboot.service.impl.resume;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.common.R;
import com.springboot.entity.resume.Resume;
import com.springboot.entity.resume.ResumeFavorite;
import com.springboot.entity.user.User;
import com.springboot.mapper.resume.ResumeFavoriteMapper;
import com.springboot.mapper.resume.ResumeMapper;
import com.springboot.service.resume.ResumeUploadService;
import com.springboot.service.statistics.StatisticsInfoService;
import com.springboot.utils.CalculateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ResumeUploadServiceImpl implements ResumeUploadService {
    private final ResumeMapper resumeMapper;

    private final ResumeFavoriteMapper resumeFavoriteMapper;

    private final StatisticsInfoService statisticsInfoService;

    private final Environment environment;

    private Boolean check_upload_count(Integer uid) {
        QueryWrapper<Resume> resumeQueryWrapper = new QueryWrapper<>();
        resumeQueryWrapper.eq("uid", uid);
        return resumeMapper.selectCount(resumeQueryWrapper) < 3;
    }

    @Override
    public R resume_upload(MultipartFile file, String summary_info, String detail_info, Float score, HttpServletRequest request) {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        if (!check_upload_count(uid)) {
            return R.error("上传简历已达上限");
        }
        String realPath;
        if (Objects.equals(environment.getActiveProfiles()[0], "dev")) {
            realPath = request.getSession().getServletContext().getRealPath("/resume/");
        } else {
            realPath = "/home/ubuntu/static/resume/";
        }

        File folder = new File(realPath);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        String old_name = file.getOriginalFilename();
        String new_name = null;
        if (old_name != null) {
            new_name = UUID.randomUUID() + old_name.substring(old_name.lastIndexOf("."));
        }
        try {
            file.transferTo(new File(folder, new_name));
            if (detail_info.length() > 2500 || summary_info.length() > 550) {
                return R.error("信息过多 请删减后上传");
            }
            Resume resume = new Resume(null, uid, summary_info, detail_info, score, new File(folder, new_name).getAbsolutePath(), null);
            resumeMapper.insert(resume);
            // 向 mongodb 同步数据
            statisticsInfoService.update_statistics_info(summary_info, 1, resume.getRid());
            return R.success("上传成功");
        } catch (IOException e) {
            return R.error("上传失败");
        }
    }

    @Override
    public R get_upload_resumes() {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        QueryWrapper<Resume> resumeQueryWrapper = new QueryWrapper<>();
        resumeQueryWrapper.eq("uid", uid).select("rid", "summary_info", "score", "create_time").orderByDesc("rid");
        List<Resume> resumeList = resumeMapper.selectList(resumeQueryWrapper);
        LocalDateTime now = LocalDateTime.now();

        List<Map<String, Object>> upload_resumes = new ArrayList<>();
        resumeList.forEach(e -> {
            Map<String, Object> info_map = new HashMap<>();
            info_map.put("rid", e.getRid());
            info_map.put("summaryInfo", e.getSummaryInfo());
            info_map.put("createTime", e.getCreateTime());
            // TODO 优化这段查询
            QueryWrapper<ResumeFavorite> resumeFavoriteQueryWrapper = new QueryWrapper<>();
            resumeFavoriteQueryWrapper.eq("rid", e.getRid());
            Long favorite_count = resumeFavoriteMapper.selectCount(resumeFavoriteQueryWrapper);
            LocalDateTime dateTime = e.getCreateTime();
            Duration duration = Duration.between(dateTime, now);
            int days = Math.toIntExact(duration.toDays());
            Double favorite_score = CalculateUtils.getFavoriteScore(days, favorite_count, e.getScore());
            info_map.put("favorite_score", favorite_score);

            upload_resumes.add(info_map);
        });
        return R.success("查询上传的简历数据成功", upload_resumes);
    }

    @Override
    public R check_before_upload() {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        if (!check_upload_count(uid)) {
            return R.error("上传简历已达上限");
        }
        return R.success("上传简历数量未达上限");
    }

    @Override
    public R del_upload_resume(Integer rid) {
        Resume del_resume = resumeMapper.selectById(rid);
        try {
            // 删文件
            String del_resume_route = del_resume.getRoute();
            File del_resume_file = new File(del_resume_route);
            del_resume_file.delete();
            // 同步 mongodb 数据
            String del_resume_summary_info = del_resume.getSummaryInfo();
            statisticsInfoService.update_statistics_info(del_resume_summary_info, -1, rid);
            // 删除收藏
            QueryWrapper<ResumeFavorite> resumeFavoriteQueryWrapper = new QueryWrapper<>();
            resumeFavoriteQueryWrapper.eq("rid", rid);
            resumeFavoriteMapper.delete(resumeFavoriteQueryWrapper);
            resumeMapper.deleteById(rid);
            return R.success("删除简历成功");
        } catch (Exception e) {
            return R.error("删除简历失败");
        }

    }
}
