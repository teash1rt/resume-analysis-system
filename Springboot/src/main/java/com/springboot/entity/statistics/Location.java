package com.springboot.entity.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "loc_coll")
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    private String id;

    @Field("location")
    private String location;

    @Field("count")
    private Integer count;
}