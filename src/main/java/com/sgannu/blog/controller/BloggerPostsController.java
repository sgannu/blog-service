package com.sgannu.blog.controller;

import com.sgannu.blog.model.mvc.BlogPost;
import com.sgannu.blog.model.mvc.BloggerPosts;
import com.sgannu.blog.service.BloggerPostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("posts")
public class BloggerPostsController {

    private BloggerPostsService bloggerPostService;

    @Autowired
    public BloggerPostsController(final BloggerPostsService service) {
        this.bloggerPostService = service;
    }

    @PostMapping("/new")
    public Mono<String> saveEntry(@RequestParam(name = "bloggerId") final String bloggerId, @RequestBody final BlogPost blogPost) {
        return bloggerPostService.newBlogPost(bloggerId, blogPost);
    }

    @PostMapping("/update")
    public Mono<String> updateEntry(@RequestParam(name = "bloggerId") final String bloggerId, @RequestBody final BlogPost blogPost) {
        return bloggerPostService.updateBlogPost(bloggerId, blogPost).flatMap(resp -> {
            return Mono.just("success");
        });
    }

    @GetMapping("/get-by-id")
    public Mono<BlogPost> getPostById(@RequestParam(name = "bloggerId") final String bloggerId, @RequestParam(name = "postId") final String postId) {
        return bloggerPostService.findPostById(bloggerId, postId);
    }

    @GetMapping("/getall-by-nickhandle")
    public Mono<BloggerPosts> getAllPostsByNickHandle(@RequestParam(name = "nickHandle") final String nickHandle) {
        return bloggerPostService.getPostsByNickHandle(nickHandle);
    }
}
