package com.springboot.mapper.resume;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.entity.resume.ResumeFavorite;
import com.springboot.vo.GetFavoriteResumesVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResumeFavoriteMapper extends BaseMapper<ResumeFavorite> {
    List<GetFavoriteResumesVO> getFavoriteResumes(Integer rid);
}
