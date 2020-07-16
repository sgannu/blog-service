package com.sgannu.blog.controller;

import com.sgannu.blog.model.Post;
import com.sgannu.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("posts")
public class PostController {

    PostService postService;

    @Autowired
    public PostController(final PostService service) {
        this.postService = service;
    }

    @PostMapping("/save")
    public Mono<String> saveOrUpdateEntry(@RequestBody final Post post) {
        return postService.save(post);
    }

    /**
     * update incase of special treatment compared to Save
     */
    @PostMapping("/update")
    public Mono<String> updateEntry(@RequestBody final Post post) {
        return postService.save(post);
    }

    @GetMapping("/get-post-id")
    public Mono<Post> getById(@RequestParam(name = "id") final String id) {
        return postService.findById(id);
    }
}
