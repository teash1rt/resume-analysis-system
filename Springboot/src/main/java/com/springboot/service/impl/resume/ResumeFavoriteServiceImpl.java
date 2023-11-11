package com.springboot.service.impl.resume;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.common.R;
import com.springboot.entity.resume.Resume;
import com.springboot.entity.resume.ResumeFavorite;
import com.springboot.entity.user.User;
import com.springboot.mapper.resume.ResumeFavoriteMapper;
import com.springboot.mapper.resume.ResumeMapper;
import com.springboot.service.resume.ResumeFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResumeFavoriteServiceImpl implements ResumeFavoriteService {

    private final ResumeFavoriteMapper resumeFavoriteMapper;

    private final ResumeMapper resumeMapper;

    @Override
    public R add_favorite(Integer rid) {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        resumeFavoriteMapper.insert(new ResumeFavorite(uid, rid, null));
        return R.success("添加收藏成功");
    }

    @Override
    public R cancel_favorite(Integer rid) {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        QueryWrapper<ResumeFavorite> resumeFavoriteQueryWrapper = new QueryWrapper<>();
        resumeFavoriteQueryWrapper.eq("uid", uid).eq("rid", rid);
        resumeFavoriteMapper.delete(resumeFavoriteQueryWrapper);
        return R.success("取消收藏成功");
    }

    @Override
    public R get_favorite() {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        QueryWrapper<ResumeFavorite> resumeFavoriteQueryWrapper = new QueryWrapper<>();
        resumeFavoriteQueryWrapper.eq("uid", uid).orderByDesc("create_time");
        List<ResumeFavorite> resumeFavorites = resumeFavoriteMapper.selectList(resumeFavoriteQueryWrapper);
        List<Map<String, Object>> favoriteInfo = new ArrayList<>();
        resumeFavorites.forEach(e -> {
            Map<String, Object> info_map = new HashMap<>();
            Resume resume = resumeMapper.selectById(e.getRid());
            info_map.put("rid", resume.getRid());
            info_map.put("summaryInfo", resume.getSummaryInfo());
            info_map.put("detailInfo", resume.getDetailInfo());
            info_map.put("resume_createTime", resume.getCreateTime());
            info_map.put("favorite_createTime", e.getCreateTime());
            favoriteInfo.add(info_map);
        });
        return R.success("收藏信息查询成功", favoriteInfo);
    }
}
