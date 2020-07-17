package com.sgannu.blog.service;

import com.sgannu.blog.model.BlogPost;
import com.sgannu.blog.model.BlogPostComment;
import com.sgannu.blog.model.BloggerPosts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
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
        Query query = Query.query(Criteria.where("id").is(bloggerId))
                .addCriteria(Criteria.where("blogPosts").elemMatch(Criteria.where("id").is(postId)));

        Mono<BloggerPosts> bloggerPosts = mongoTemplate.findOne(query, BloggerPosts.class);
        return bloggerPosts.map(posts -> posts.getBlogPosts().get(0));
    }

    public Mono<BloggerPosts> newBlogPost(String bloggerId, BlogPost blogPost) {
        Query query = Query.query(Criteria.where("id").is(bloggerId));
        Update update = new Update().addToSet("blogPosts", blogPost);
        return mongoTemplate.findAndModify(query, update, BloggerPosts.class);
    }

    public Mono<BloggerPosts> updateBlogPost(String bloggerId, BlogPost blogPost) {
        Query query = Query.query(Criteria.where("id").is(bloggerId));
        Update update = new Update().pull("blogPosts", blogPost).push("blogPosts", blogPost);

        return mongoTemplate.findAndModify(query, update, BloggerPosts.class);
    }

    /**
     * Enhancement: Flux can be used to get large number of posts with auto-scroll on web page
     */
    public Mono<BloggerPosts> getPostsByNickHandle(String nickHandle) {
        Query query = Query.query(Criteria.where("nickHandle").is(nickHandle));

        // TODO apply sort with aggregation on subdocuments or implement client side sorting.
        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.DESC, "publishDate"));
        return mongoTemplate.findOne(query, BloggerPosts.class);
    }

    public Mono<BloggerPosts> commentOnPost(String commentOnId, String postId, BlogPostComment comment) {
        Query query = Query.query(Criteria.where("id").is(commentOnId))
                .addCriteria(Criteria.where("blogPosts").elemMatch(Criteria.where("id").is(postId)));
        Update update = new Update().addToSet("comments", comment);

        return mongoTemplate.findAndModify(query, update, BloggerPosts.class);

    }
}
