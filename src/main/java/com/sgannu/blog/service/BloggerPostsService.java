package com.sgannu.blog.service;

import com.sgannu.blog.model.BlogPost;
import com.sgannu.blog.model.BloggerPosts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BloggerPostsService {

    private ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public void BloggerPostsService(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Mono<BlogPost> findPostById(String bloggerId, String postId) {
        Query query = new Query(Criteria.where("_id").is(bloggerId))
                .addCriteria(Criteria.where("blogPosts._id").is(postId));

        Mono<BloggerPosts> bloggerPosts = mongoTemplate.findOne(query, BloggerPosts.class);
        return bloggerPosts.map(posts -> posts.getBlogPosts().get(0));
    }

    public Mono<BloggerPosts> newBlogPost(String bloggerId, BlogPost blogPost) {
        Query query = new Query(Criteria.where("_id").is(bloggerId));
        Update update = new Update().addToSet("blogPosts", blogPost);
        return mongoTemplate.findAndModify(query, update, BloggerPosts.class);
    }

    public Mono<BloggerPosts> updateBlogPost(String bloggerId, BlogPost blogPost) {
        Query query = new Query(Criteria.where("_id").is(bloggerId));
        Update update = new Update().pull("blogPosts", blogPost).pull("blogPosts", blogPost);

        return mongoTemplate.findAndModify(query, update, BloggerPosts.class);
    }

    public Mono<BloggerPosts> newUserEntry(BloggerPosts bloggerPost) {
        return mongoTemplate.save(bloggerPost);
    }

}
