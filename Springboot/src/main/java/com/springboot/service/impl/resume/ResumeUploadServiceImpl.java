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

    @Override
    public R resumeUpload(MultipartFile file, String summary_info, String detail_info, Float score, HttpServletRequest request) {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        String realPath = Objects.equals(environment.getActiveProfiles()[0], "dev") ?
                request.getSession().getServletContext().getRealPath("/resume/") :
                "/home/ubuntu/static/resume/";

        File folder = new File(realPath);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = null;
        if (oldName != null) {
            newName = UUID.randomUUID() + oldName.substring(oldName.lastIndexOf("."));
        }
        try {
            file.transferTo(new File(folder, newName));
            if (detail_info.length() > 2500 || summary_info.length() > 550) {
                return R.error("信息过多 请删减后上传");
            }
            Resume resume = new Resume(null, uid, summary_info, detail_info, score, new File(folder, newName).getAbsolutePath(), null);
            resumeMapper.insert(resume);
            // 向 mongodb 同步数据
            statisticsInfoService.update_statistics_info(summary_info, 1, resume.getRid());
            return R.success("上传成功");
        } catch (IOException e) {
            return R.error("上传失败");
        }
    }

    @Override
    public R getUploadResumes() {
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
    public R delUploadResume(Integer rid) {
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
