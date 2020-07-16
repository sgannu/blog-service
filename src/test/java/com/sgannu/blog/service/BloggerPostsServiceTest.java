package com.sgannu.blog.service;

import com.sgannu.blog.model.BlogPost;
import com.sgannu.blog.model.BloggerPosts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(MockitoExtension.class)
public class BloggerPostsServiceTest {

    public static final String TEST_ID = "test-id";
    @InjectMocks
    private BloggerPostsService serviceUnderTest;
    @Mock
    private ReactiveMongoTemplate mongoTemplate;
    private BlogPost blogPostEntry;
    private BloggerPosts bloggerPosts;

    @BeforeEach
    void setUp() {
        blogPostEntry = BlogPost.builder().title("title").build();
        bloggerPosts = BloggerPosts.builder().blogPosts(Arrays.asList(blogPostEntry)).build();
    }

    @Test
    void whenSavePostCalledThenShouldBeSavedOnRepo() {
        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), any())).thenReturn(Mono.just(bloggerPosts));
        Mono<BloggerPosts> postId = serviceUnderTest.newBlogPost(TEST_ID, blogPostEntry);
        StepVerifier.create(postId)
                .consumeNextWith(response -> assertNotNull(response))
                .verifyComplete();
    }

    @Disabled
    @Test
    void whenUpdatePostCalledThenShouldBeSavedOnRepo() {
        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), any())).thenReturn(Mono.just(bloggerPosts));
        Mono<BloggerPosts> postResponse = serviceUnderTest.updateBlogPost(TEST_ID, blogPostEntry);
        StepVerifier.create(postResponse)
                .consumeNextWith(response -> assertTrue(response.equals(TEST_ID)))
                .verifyComplete();
    }

    @Test
    void whenGetByIdCalledThenShouldReturnPost() {
        when(mongoTemplate.findOne(any(Query.class), any())).thenReturn(Mono.just(bloggerPosts));
        Mono<BlogPost> postResponse = serviceUnderTest.findPostById(TEST_ID, TEST_ID);
        StepVerifier.create(postResponse)
                .consumeNextWith(response -> assertEquals(blogPostEntry, response))
                .verifyComplete();
    }

}