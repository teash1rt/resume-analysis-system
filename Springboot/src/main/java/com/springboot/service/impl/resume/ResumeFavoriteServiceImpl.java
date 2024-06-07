package com.springboot.service.impl.resume;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.common.R;
import com.springboot.entity.resume.ResumeFavorite;
import com.springboot.entity.user.User;
import com.springboot.mapper.resume.ResumeFavoriteMapper;
import com.springboot.service.resume.ResumeFavoriteService;
import com.springboot.vo.GetFavoriteResumesVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeFavoriteServiceImpl implements ResumeFavoriteService {
    private final ResumeFavoriteMapper resumeFavoriteMapper;

    @Override
    public R favoriteResume(Integer rid, Boolean isFavorite) {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        QueryWrapper<ResumeFavorite> resumeFavoriteQueryWrapper = new QueryWrapper<>();
        resumeFavoriteQueryWrapper.eq("uid", uid).eq("rid", rid);
        if (isFavorite) {
            if (!resumeFavoriteMapper.exists(resumeFavoriteQueryWrapper)) {
                resumeFavoriteMapper.insert(new ResumeFavorite(uid, rid, null));
            }
            return R.success("添加收藏成功");
        }

        resumeFavoriteMapper.delete(resumeFavoriteQueryWrapper);
        return R.success("取消收藏成功");
    }

    @Override
    public R getFavoriteResumes() {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUid();
        List<GetFavoriteResumesVO> favoriteResumes = resumeFavoriteMapper.getFavoriteResumes(uid);
        return R.success("收藏信息查询成功", favoriteResumes);
    }
}
