package com.example.blogrestapi.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {
    private long id;

    @NotEmpty
    @Size(min=2,message = "Title must be atleast 2 characters")
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    @Size(min=10,message = "Description must be atleast 10 characters")
    private String description;

    private Set<CommentDto> comments;

    private Long categoryId;
}
