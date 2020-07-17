package com.sgannu.blog.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "blogger-posts")
@Builder
public class BloggerPosts {
    @Id
    private ObjectId id;
    private String nickHandle;
    List<BlogPost> blogPosts;
}
