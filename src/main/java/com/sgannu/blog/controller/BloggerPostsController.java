package com.sgannu.blog.controller;

import com.sgannu.blog.model.BlogPost;
import com.sgannu.blog.service.BloggerPostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("posts")
public class BloggerPostsController {

    BloggerPostsService bloggerPostService;

    @Autowired
    public BloggerPostsController(final BloggerPostsService service) {
        this.bloggerPostService = service;
    }

    @PostMapping("/new")
    public Mono<String> saveOrUpdateEntry(@RequestParam(name = "bloggerId") final String bloggerId, @RequestBody final BlogPost blogPost) {
        return bloggerPostService.newBlogPost(bloggerId, blogPost).map(resp -> {
           return "success";
        });
    }

    @PostMapping("/update")
    public Mono<String> updateEntry(@RequestParam(name = "bloggerId") final String bloggerId, @RequestBody final BlogPost blogPost) {
        return bloggerPostService.updateBlogPost(bloggerId, blogPost).map(resp -> {
            return "success";
        });
    }

    @GetMapping("/get-by-id")
    public Mono<BlogPost> getPostById(@RequestParam(name = "bloggerId") final String bloggerId, @RequestParam(name = "postId") final String postId) {
        return bloggerPostService.findPostById(bloggerId, postId);
    }
}
