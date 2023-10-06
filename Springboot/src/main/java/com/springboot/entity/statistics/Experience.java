package com.springboot.entity.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;

@Data
@Document(collection = "experience_coll")
@NoArgsConstructor
@AllArgsConstructor
public class Experience {
    @Id
    private String id;

    @Field("rids")
    private ArrayList<Integer> rids;

    @Field("total_company_count")
    private ArrayList<Integer> total_company_count;

    @Field("total_work_time")
    private ArrayList<Integer> total_work_time;
}
