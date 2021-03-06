package com.sgannu.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgannu.blog.model.BlogPost;
import com.sgannu.blog.model.BlogPostComment;
import com.sgannu.blog.model.BloggerPosts;
import com.sgannu.blog.service.BloggerPostsService;
import com.sgannu.blog.service.BloggerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("IntegrationTest")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BlogServiceIntegrationTest {

    public static final String TEST_ID = "test-id";
    @Autowired
    private WebTestClient mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ReactiveMongoTemplate mongoTemplate;
    @Autowired
    private BloggerPostsService bloggerPostsService;
    @Autowired
    private BloggerService bloggerService;

    private BlogPost blogPostEntry;
    private BloggerPosts bloggerPosts;
    private BlogPostComment blogPostComment;

    @BeforeEach
    void setup() {
        blogPostEntry = BlogPost.builder().content("TEST").build();
        bloggerPosts = BloggerPosts.builder().nickHandle("nickname").blogPosts(Arrays.asList(blogPostEntry)).build();
        blogPostComment = BlogPostComment.builder().byNickHandle(TEST_ID).comment("Comment").build();

        mongoTemplate.dropCollection(BloggerPosts.class);
        createPostData();
    }
    @Test
    void newBlogPost() throws Exception {
        mockMvc.post().uri("posts/new?bloggerId="+TEST_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(blogPostEntry), BlogPost.class)
                .exchange()
                .expectStatus()
                .isOk();

        validateReactiveResponse();
    }

    @Test
    void updateBlogPost() throws Exception {
        blogPostEntry.setContent("UPDATED");
        mockMvc.post().uri("posts/update?bloggerId="+TEST_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(blogPostEntry), BlogPost.class)
                .exchange()
                .expectStatus()
                .isOk();

        validateReactiveResponse();
    }

    @Test
    void getBlogPostById() throws Exception {

        mockMvc.get().uri(String.format("posts/get-by-id?bloggerId=%s&postId=%s", TEST_ID, TEST_ID))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        Mono<BlogPost> postEntity = bloggerPostsService.findPostById(TEST_ID, TEST_ID);
        StepVerifier.create(postEntity)
                .verifyComplete();
    }

    @Test
    void getPostsByNickHandle() {
        mockMvc.get().uri("posts/get-by-nickhandle?nickHandle=nickname")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        Mono<BloggerPosts> postEntity = bloggerPostsService.getPostsByNickHandle("nickname");
        StepVerifier.create(postEntity)
                .consumeNextWith(response -> assertEquals(bloggerPosts, response))
                .verifyComplete();
    }

    @Test
    void comment() {
        mockMvc.post().uri(String.format("posts/comment?commentOnId=%s&postId=%s", TEST_ID, TEST_ID))
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(blogPostComment), BlogPost.class)
                .exchange()
                .expectStatus()
                .isOk();

        Mono<BloggerPosts> postEntity = bloggerPostsService.getPostsByNickHandle("nickname");
        StepVerifier.create(postEntity)
                .consumeNextWith(resp -> assertNotNull(resp))
                // .consumeNextWith(response -> assertEquals(blogPostComment, response.getBlogPosts().get(0).getComments().get(0)))
                .verifyComplete();
    }

    private void createPostData() {
        Mono<BloggerPosts> postEntity = bloggerService.newBlogger(bloggerPosts);
        StepVerifier.create(postEntity)
                .consumeNextWith(response -> assertEquals(bloggerPosts, response))
                .verifyComplete();
    }

    private void validateReactiveResponse() {
        Mono<BlogPost> postEntity = bloggerPostsService.findPostById(TEST_ID, TEST_ID);
        StepVerifier.create(postEntity)
                .verifyComplete();

    }
}
