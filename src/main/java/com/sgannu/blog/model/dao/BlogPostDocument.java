package com.sgannu.blog.model.dao;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document
@Builder
public class BlogPostDocument {
    @Id
    private ObjectId _id;
    private String title;
    private String content;
    private Date publishDate;
    private List<BlogPostCommentDocument> comments;
}
