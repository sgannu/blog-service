package com.sgannu.blog.service;

import com.sgannu.blog.model.Post;
import com.sgannu.blog.repo.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    public static final String TEST_ID = "test-id";
    @InjectMocks
    private PostService objUnderTest;
    @Mock
    private PostRepository repository;
    private Post postEntry;

    @BeforeEach
    void setUp() {
        postEntry = Post.builder().id(TEST_ID).build();
    }

    @Test
    void whenSavePostCalledThenShouldBeSavedOnRepo() {
        when(repository.save(any())).thenReturn(Mono.just(postEntry));
        Mono<String> postId = objUnderTest.save(postEntry);
        StepVerifier.create(postId)
                .consumeNextWith(response -> assertTrue(response.equals(TEST_ID)))
                .verifyComplete();
    }

    @Test
    void whenGetByIdCalledThenShouldReturnPost() {
        when(repository.findById(anyString())).thenReturn(Mono.just(postEntry));
        Mono<Post> postResponse = objUnderTest.findById(TEST_ID);
        StepVerifier.create(postResponse)
                .consumeNextWith(response -> assertEquals(postEntry, response))
                .verifyComplete();
    }

}