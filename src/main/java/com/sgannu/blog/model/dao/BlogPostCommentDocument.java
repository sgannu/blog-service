package com.sgannu.blog.model.dao;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@Builder
public class BlogPostCommentDocument {
    private String content;
    private String byUserNickHandle;
}
