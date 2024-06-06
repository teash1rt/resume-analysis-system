package com.springboot.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PageInfoVO {
    private Integer rid;
    private String username;
    private String summaryInfo;
    private Timestamp createTime;
    private Integer isFavorite;
}
