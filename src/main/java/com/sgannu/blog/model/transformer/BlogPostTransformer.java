package com.sgannu.blog.model.transformer;

import com.sgannu.blog.model.dao.BlogPostDocument;
import com.sgannu.blog.model.mvc.BlogPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlogPostTransformer {

    @Autowired
    BlogPostCommentTransformer blogPostCommentTransformer;

    public BlogPostDocument apply(BlogPost blogPost) {
        return BlogPostDocument.builder()
                .title(blogPost.getTitle())
                .content(blogPost.getContent())
                .publishDate(blogPost.getPublishDate())
                .comments(blogPostCommentTransformer.toDocuments(blogPost.getComments()))
                .build();
    }

    public BlogPost apply(BlogPostDocument blogPost) {
        return BlogPost.builder()
                .id(blogPost.get_id().toString())
                .title(blogPost.getTitle())
                .content(blogPost.getContent())
                .publishDate(blogPost.getPublishDate())
                .comments(blogPostCommentTransformer.apply(blogPost.getComments()))
                .build();
    }

    public List<BlogPostDocument> toDocuments(List<BlogPost> bloggerPosts) {
        return bloggerPosts.stream().map(this::apply).collect(Collectors.toList());
    }

    public List<BlogPost> apply(List<BlogPostDocument> bloggerPosts) {
        return bloggerPosts.stream().map(this::apply).collect(Collectors.toList());
    }
}
