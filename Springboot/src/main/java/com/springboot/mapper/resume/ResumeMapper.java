package com.springboot.mapper.resume;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.entity.resume.Resume;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResumeMapper extends BaseMapper<Resume> {
}
