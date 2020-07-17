package com.sgannu.blog.model.transformer;

import com.sgannu.blog.model.dao.BloggerPostsDocument;
import com.sgannu.blog.model.mvc.BloggerPosts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BloggerPostsTransformer {

    @Autowired
    BlogPostTransformer blogPostTransformer;

    public BloggerPostsDocument apply(BloggerPosts bloggerPosts) {
        return BloggerPostsDocument.builder()
                .nickHandle(bloggerPosts.getNickHandle())
                .blogPosts(blogPostTransformer.toDocuments(bloggerPosts.getBlogPosts()))
                .build();
    }

    public BloggerPosts apply(BloggerPostsDocument bloggerPosts) {
        return BloggerPosts.builder()
                .id(bloggerPosts.get_id().toString())
                .nickHandle(bloggerPosts.getNickHandle())
                .blogPosts(blogPostTransformer.apply(bloggerPosts.getBlogPosts()))
                .build();
    }
}
