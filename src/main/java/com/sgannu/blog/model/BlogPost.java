package com.sgannu.blog.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
@Builder
public class BlogPost {
    @Id
    private ObjectId _id;
    private String title;
    private String content;
    private Date publishDate;
}
