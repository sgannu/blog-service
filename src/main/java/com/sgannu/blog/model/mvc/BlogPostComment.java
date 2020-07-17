package com.sgannu.blog.model.mvc;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogPostComment {
    private String content;
    private String byUserNickHandle;
}
