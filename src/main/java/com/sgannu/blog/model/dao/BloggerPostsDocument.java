package com.sgannu.blog.model.dao;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "blogger-posts")
@Builder
public class BloggerPostsDocument {
    @Id
    private ObjectId _id;
    private String nickHandle;
    List<BlogPostDocument> blogPosts;
}
