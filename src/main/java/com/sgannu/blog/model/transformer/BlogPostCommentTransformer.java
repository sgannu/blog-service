package com.sgannu.blog.model.transformer;

import com.sgannu.blog.model.dao.BlogPostCommentDocument;
import com.sgannu.blog.model.mvc.BlogPostComment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlogPostCommentTransformer {
    public BlogPostCommentDocument apply(BlogPostComment blogPost) {
        return BlogPostCommentDocument.builder()
                .content(blogPost.getContent())
                .byUserNickHandle(blogPost.getByUserNickHandle())
                .build();
    }

    public BlogPostComment apply(BlogPostCommentDocument blogPost) {
        return BlogPostComment.builder()
                .content(blogPost.getContent())
                .byUserNickHandle(blogPost.getByUserNickHandle())
                .build();
    }

    public List<BlogPostCommentDocument> toDocuments(List<BlogPostComment> blogPosts) {
        List<BlogPostCommentDocument> list = new ArrayList<>();
        return blogPosts.stream().map(this::apply).collect(Collectors.toList());
    }
    public List<BlogPostComment> apply(List<BlogPostCommentDocument> blogPosts) {
        List<BlogPostComment> list = new ArrayList<>();
        return blogPosts.stream().map(this::apply).collect(Collectors.toList());
    }
}
