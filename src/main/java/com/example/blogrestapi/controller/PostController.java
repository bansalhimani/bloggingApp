package com.example.blogrestapi.controller;

import com.example.blogrestapi.payload.PostDto;
import com.example.blogrestapi.service.PostService;
import com.example.blogrestapi.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;
    public PostController(PostService postService){
        this.postService=postService;
    }


    //create blog post
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto),org.springframework.http.HttpStatus.CREATED);
    }

    @GetMapping
    public List<PostDto> getAllPosts(@RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required=false) int pageNo,
                                     @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
                                     @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortBy,
                                     @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIR,required = false) String sortDir){
        return postService.getAllPosts(pageNo,pageSize,sortBy,sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable long id){
        return new ResponseEntity<>(postService.getPostById(id),org.springframework.http.HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable long id,@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.updatePost(id,postDto),org.springframework.http.HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable long id){
        postService.deletePost(id);
        return new ResponseEntity<>("Post deleted successfully",org.springframework.http.HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable long categoryId){
        List<PostDto> posts =postService.getPostsByCategory(categoryId);
        return ResponseEntity.ok(posts);
    }

}
