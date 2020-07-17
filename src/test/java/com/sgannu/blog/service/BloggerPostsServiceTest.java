package com.sgannu.blog.service;

import com.sgannu.blog.model.dao.BlogPostDocument;
import com.sgannu.blog.model.dao.BloggerPostsDocument;
import com.sgannu.blog.model.mvc.BlogPost;
import com.sgannu.blog.model.mvc.BloggerPosts;
import com.sgannu.blog.model.transformer.BlogPostCommentTransformer;
import com.sgannu.blog.model.transformer.BlogPostTransformer;
import com.sgannu.blog.model.transformer.BloggerPostsTransformer;
import org.bson.types.ObjectId;
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
    @Mock
    private BloggerPostsTransformer bloggerPostsTransformer;
    @Mock
    private BlogPostCommentTransformer blogPostCommentTransformer;
    @Mock
    private BlogPostTransformer blogPostTransformer;

    private BlogPost blogPostEntry;
    private BloggerPosts bloggerPosts;
    private BlogPostDocument blogPostDocument;
    private BloggerPostsDocument bloggerPostsDocument;

    @BeforeEach
    void setUp() {
        blogPostEntry = BlogPost.builder().title("title").build();
        bloggerPosts = BloggerPosts.builder().nickHandle("nickname").blogPosts(Arrays.asList(blogPostEntry)).build();
        blogPostDocument = BlogPostDocument.builder()._id(new ObjectId()).title("title").build();
        bloggerPostsDocument = BloggerPostsDocument.builder()._id(new ObjectId()).nickHandle("nickname").blogPosts(Arrays.asList(blogPostDocument)).build();
    }

    @Test
    void whenSavePostCalledThenShouldBeSavedOnRepo() {
        when(blogPostTransformer.apply(blogPostEntry)).thenReturn(blogPostDocument);
        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), any())).thenReturn(Mono.just(bloggerPostsDocument));
        Mono<String> postId = serviceUnderTest.newBlogPost(TEST_ID, blogPostEntry);
        StepVerifier.create(postId)
                .consumeNextWith(response -> assertNotNull(response))
                .verifyComplete();
    }

    @Disabled
    @Test
    void whenUpdatePostCalledThenShouldBeSavedOnRepo() {
        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), any())).thenReturn(Mono.just(bloggerPostsDocument));
        Mono<BloggerPosts> postResponse = serviceUnderTest.updateBlogPost(TEST_ID, blogPostEntry);
        StepVerifier.create(postResponse)
                .consumeNextWith(response -> assertTrue(response.equals(TEST_ID)))
                .verifyComplete();
    }

    @Test
    void whenGetByIdCalledThenShouldReturnPost() {
        when(blogPostTransformer.apply(blogPostDocument)).thenReturn(blogPostEntry);
        when(mongoTemplate.findOne(any(Query.class), any())).thenReturn(Mono.just(blogPostDocument));
        Mono<BlogPost> postResponse = serviceUnderTest.findPostById(TEST_ID, TEST_ID);
        StepVerifier.create(postResponse)
                .consumeNextWith(response -> assertEquals(blogPostEntry, response))
                .verifyComplete();
    }

}