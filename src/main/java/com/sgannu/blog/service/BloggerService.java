package com.sgannu.blog.service;

import com.sgannu.blog.model.BloggerPosts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BloggerService {
    private ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public void BloggerPostsService(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Mono<BloggerPosts> newBlogger(BloggerPosts blogger) {
        return mongoTemplate.save(blogger);
    }
}
