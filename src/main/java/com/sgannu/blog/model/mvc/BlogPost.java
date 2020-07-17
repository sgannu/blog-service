package com.sgannu.blog.model.mvc;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class BlogPost {
    private String id;
    private String title;
    private String content;
    private Date publishDate;
    private List<BlogPostComment> comments;
}
