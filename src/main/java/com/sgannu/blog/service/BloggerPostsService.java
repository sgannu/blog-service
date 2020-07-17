package com.sgannu.blog.service;

import com.sgannu.blog.model.dao.BlogPostDocument;
import com.sgannu.blog.model.dao.BloggerPostsDocument;
import com.sgannu.blog.model.mvc.BlogPost;
import com.sgannu.blog.model.mvc.BloggerPosts;
import com.sgannu.blog.model.transformer.BlogPostCommentTransformer;
import com.sgannu.blog.model.transformer.BlogPostTransformer;
import com.sgannu.blog.model.transformer.BloggerPostsTransformer;
import org.bson.types.ObjectId;
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
    private BloggerPostsTransformer bloggerPostsTransformer;
    private BlogPostCommentTransformer blogPostCommentTransformer;
    private BlogPostTransformer blogPostTransformer;

    @Autowired
    public BloggerPostsService(ReactiveMongoTemplate mongoTemplate,
                               BloggerPostsTransformer bloggerPostsTransformer, BlogPostCommentTransformer blogPostCommentTransformer, BlogPostTransformer blogPostTransformer) {
        this.mongoTemplate = mongoTemplate;
        this.bloggerPostsTransformer = bloggerPostsTransformer;
        this.blogPostCommentTransformer = blogPostCommentTransformer;
        this.blogPostTransformer = blogPostTransformer;
    }

    public Mono<BlogPost> findPostById(String bloggerId, String postId) {
        Query query = new Query(Criteria.where("_id").is(bloggerId))
                .addCriteria(Criteria.where("data").elemMatch(Criteria.where("_id").is(postId)));

        Mono<BlogPostDocument> bloggerPosts = mongoTemplate.findOne(query, BlogPostDocument.class);
        return bloggerPosts.flatMap(resp -> blogPostTransformer.apply(resp));
    }

    public Mono<String> newBlogPost(String bloggerId, BlogPost blogPost) {
        ObjectId blogPostId = new ObjectId();
        BlogPostDocument blogPostDocument = blogPostTransformer.apply(blogPost);
        blogPostDocument.set_id(blogPostId);

        Query query = new Query(Criteria.where("_id").is(bloggerId));
        Update update = new Update().addToSet("blogPosts", blogPostDocument);
        return mongoTemplate.findAndModify(query, update, BloggerPostsDocument.class).flatMap(val -> Mono.just(val.toString()));
    }

    public Mono<BloggerPosts> updateBlogPost(String bloggerId, BlogPost blogPost) {
        Query query = new Query(Criteria.where("_id").is(bloggerId));
        Update update = new Update().pull("blogPosts", blogPost).push("blogPosts", blogPost);

        return mongoTemplate.findAndModify(query, update, BloggerPostsDocument.class)
                .flatMap( resp -> Mono.just(bloggerPostsTransformer.apply(resp)));
    }

    public Mono<BloggerPosts> getPostsByNickHandle(String nickHandle) {
        Query query = new Query(Criteria.where("nickHandle").is(nickHandle));
        return mongoTemplate.findOne(query, BloggerPostsDocument.class)
                .flatMap( resp -> Mono.just(bloggerPostsTransformer.apply(resp)));
    }

}
