package com.springboot.entity.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "edu_coll")
@NoArgsConstructor
@AllArgsConstructor
public class Education {
    @Id
    private String id;

    @Field("education")
    private String education;

    @Field("count")
    private Integer count;
}
