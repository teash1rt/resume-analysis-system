package com.springboot.vo;

import lombok.Data;

@Data
public class GetFavoriteResumesVO {
    private Integer rid;
    private String summaryInfo;
    private String createTime;
}
