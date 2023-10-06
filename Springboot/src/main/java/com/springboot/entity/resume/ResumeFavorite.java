package com.springboot.entity.resume;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("resumefavorite")
@NoArgsConstructor
@AllArgsConstructor
public class ResumeFavorite {
    private Integer uid;
    private Integer rid;
    private LocalDateTime createTime;
}
