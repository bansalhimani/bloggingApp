package com.example.blogrestapi.service;

import com.example.blogrestapi.payload.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);
    List<PostDto> getAllPosts(int pageNo,int pageSize,String sortBy,String sortDir);
    PostDto getPostById(long id);
    PostDto updatePost(long id,PostDto postDto);
    void deletePost(long id);
    List<PostDto> getPostsByCategory(long categoryId);
}
