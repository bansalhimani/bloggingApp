package com.example.blogrestapi.service.implementation;

import com.example.blogrestapi.exception.ResourceNotFoundException;
import com.example.blogrestapi.model.Category;
import com.example.blogrestapi.model.Post;
import com.example.blogrestapi.payload.PostDto;
import com.example.blogrestapi.repository.CategoryRepository;
import com.example.blogrestapi.repository.PostRepository;
import com.example.blogrestapi.service.PostService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper mapper;
    private CategoryRepository categoryRepository;

    public PostServiceImpl(PostRepository postRepository,ModelMapper mapper,CategoryRepository categoryRepository){
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }
    @Override
    public PostDto createPost(PostDto postDto) {
        Category category = categoryRepository.findById(postDto.getCategoryId()).
                orElseThrow(()->new ResourceNotFoundException("Category not found","id",postDto.getCategoryId()));
        Post post= convertDtoToEntity(postDto);
        post.setCategory(category);
        Post savedPost = postRepository.save(post);
        return convertEntityToDto(savedPost);
    }

    @Override
    public List<PostDto> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        Page<Post> posts = postRepository.findAll(pageable);
        List<Post> postList = posts.getContent();
        return postList.stream().map(post -> convertEntityToDto(post)).collect(Collectors.toList());
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post not found","id",id));
        return convertEntityToDto(post);
    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post not found","id",id));
        Category category=categoryRepository.findById(postDto.getCategoryId()).
                orElseThrow(()->new ResourceNotFoundException("Category not found","id",postDto.getCategoryId()));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        post.setCategory(category);
        Post updatedPost = postRepository.save(post);
        return convertEntityToDto(updatedPost);
    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post not found","id",id));
        postRepository.delete(post);
    }

    @Override
    public List<PostDto> getPostsByCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(()->new ResourceNotFoundException("Category not found","id",categoryId));
        List<Post> posts = postRepository.findByCategoryId(categoryId);
        return posts.stream().map(post -> convertEntityToDto(post)).collect(Collectors.toList());
    }

    private PostDto convertEntityToDto(Post post){
        PostDto postDto = mapper.map(post,PostDto.class);
/*        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setDescription(post.getDescription());*/
        return postDto;
    }

    private Post convertDtoToEntity(PostDto postDto){
        Post post = mapper.map(postDto,Post.class);
/*        Post post = new Post();
        post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());*/
        return post;
    }
}
