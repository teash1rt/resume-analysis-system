package com.springboot.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetPageInfoVO {
    private Integer total;
    private List<PageInfoVO> pageInfo;
}
