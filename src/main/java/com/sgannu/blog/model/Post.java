package com.sgannu.blog.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
@Builder
public class Post {
    @Id
    private String id;
    private String title;
    private String content;
    Date publishDate;
}
