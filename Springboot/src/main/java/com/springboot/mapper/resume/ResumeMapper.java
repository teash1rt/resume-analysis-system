package com.springboot.mapper.resume;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.entity.resume.Resume;
import com.springboot.vo.PageInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResumeMapper extends BaseMapper<Resume> {
    List<PageInfoVO> getPageInfo(Integer uid, Integer sortOrder);
}
