package com.sgannu.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgannu.blog.model.Post;
import com.sgannu.blog.repo.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    private PostRepository postRepository;

    private Post postData;

    @BeforeEach
    void setup() {
        postData = Post.builder().id(TEST_ID).build();
    }
    @Test
    void saveBlogPost() throws Exception {
        mockMvc.post().uri("posts/save")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(postData), Post.class)
                .exchange()
                .expectStatus()
                .isOk();

        Mono<Post> postEntity = postRepository.findById(TEST_ID);
        validateReactiveResponse(postEntity);
    }

    @Test
    void updateBlogPost() throws Exception {
        createPostData();
        postData.setContent("TEST");
        mockMvc.post().uri("posts/save")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(postData), Post.class)
                .exchange()
                .expectStatus()
                .isOk();

        Mono<Post> postEntity = postRepository.findById(TEST_ID);
        validateReactiveResponse(postEntity);
    }

    @Test
    void getBlogPostById() throws Exception {
        createPostData();

        mockMvc.get().uri("posts/get-post-id?id="+TEST_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        Mono<Post> getEntity = postRepository.findById(TEST_ID);
        validateReactiveResponse(getEntity);
    }

    private void createPostData() {
        Post postData = Post.builder().id(TEST_ID).build();
        Mono<Post> postEntity = postRepository.save(postData);
        validateReactiveResponse(postEntity);
    }

    private void validateReactiveResponse(Mono<Post> postEntity) {
        StepVerifier.create(postEntity)
                .consumeNextWith(response -> assertEquals(postData, response))
                .verifyComplete();
    }
}
