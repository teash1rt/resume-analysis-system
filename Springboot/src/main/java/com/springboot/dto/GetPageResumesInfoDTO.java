package com.springboot.dto;

import lombok.Data;

@Data
public class GetPageResumesInfoDTO {
    private Integer page;
    private Integer pageSize;
    private Integer sortOrder;
}
