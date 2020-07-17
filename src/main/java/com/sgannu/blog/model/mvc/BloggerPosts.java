package com.sgannu.blog.model.mvc;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BloggerPosts {
    private String id;
    private String nickHandle;
    List<BlogPost> blogPosts;

}
