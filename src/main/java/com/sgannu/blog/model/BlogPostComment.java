package com.sgannu.blog.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@Builder
public class BlogPostComment {
    private String comment;
    private String byNickHandle;
}
