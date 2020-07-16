package com.sgannu.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgannu.blog.model.Post;
import com.sgannu.blog.repo.PostRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    void saveBlogPost() throws Exception {
        Post postData = Post.builder().id(TEST_ID).build();

        mockMvc.post().uri("posts/save")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(postData), Post.class)
                .exchange()
                .expectStatus()
                .isOk();

        Mono<Post> postEntity = postRepository.findById(TEST_ID);
        StepVerifier.create(postEntity)
                .consumeNextWith(response -> assertEquals(TEST_ID, response.getId()))
                .verifyComplete();
    }
}
