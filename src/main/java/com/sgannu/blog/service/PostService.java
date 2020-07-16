package com.sgannu.blog.service;

import com.sgannu.blog.model.Post;
import com.sgannu.blog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PostService {

    private PostRepository repository;

    @Autowired
    public void PostService(PostRepository repository) {
        this.repository = repository;
    }

    public Mono<Post> findById(String id) {
        Mono<Post> post = repository.findById(id);
        return post;
    }

    public Mono<String> save(Post post) {
        Mono<Post> response = repository.save(post);
        return response.map( resp -> {
            return resp.getId();
        });
    }
}
