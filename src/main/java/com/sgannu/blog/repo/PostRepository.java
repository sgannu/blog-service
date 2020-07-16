package com.sgannu.blog.repo;

import com.sgannu.blog.model.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository
        extends ReactiveMongoRepository<Post, String> { }
