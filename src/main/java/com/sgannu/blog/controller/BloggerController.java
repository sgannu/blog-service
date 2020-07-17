package com.sgannu.blog.controller;


import com.sgannu.blog.model.mvc.BloggerPosts;
import com.sgannu.blog.service.BloggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("blogger")
public class BloggerController {

    private BloggerService bloggerService;

    @Autowired
    public BloggerController(final BloggerService bloggerService) {
        this.bloggerService = bloggerService;
    }

    @PostMapping("/new")
    public Mono<String> saveEntry( @RequestBody final BloggerPosts blogger) {
        return bloggerService.newBlogger(blogger).flatMap(resp -> {
            return resp.getId();
        });
    }
}
