package com.springboot.entity.resume;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("resume")
@NoArgsConstructor
@AllArgsConstructor
public class Resume {
    @TableId(type = IdType.AUTO)
    private Integer rid;
    private Integer uid;
    private String summaryInfo;
    private String detailInfo;
    private Float score;
    private String route;
    private LocalDateTime createTime;
}
