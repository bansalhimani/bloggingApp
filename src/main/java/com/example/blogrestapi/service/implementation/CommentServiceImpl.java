package com.example.blogrestapi.service.implementation;

import com.example.blogrestapi.exception.BlogApiException;
import com.example.blogrestapi.exception.ResourceNotFoundException;
import com.example.blogrestapi.model.Comment;
import com.example.blogrestapi.model.Post;
import com.example.blogrestapi.payload.CommentDto;
import com.example.blogrestapi.repository.CommentRepository;
import com.example.blogrestapi.repository.PostRepository;
import com.example.blogrestapi.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private ModelMapper mapper;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository,PostRepository postRepository,ModelMapper mapper){
        this.commentRepository=commentRepository;
        this.postRepository=postRepository;
        this.mapper=mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found","id",postId));
        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);
        return maptoDto(savedComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(comment -> maptoDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found","id",postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment not found","id",commentId));
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment doesnot belong to post");
        }
        return maptoDto(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found","id",postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment not found","id",commentId));
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment doesnot belong to post");
        }
        comment.setName(commentDto.getName());
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());
        Comment updatedComment = commentRepository.save(comment);
        return maptoDto(updatedComment);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found","id",postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment not found","id",commentId));
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment doesnot belong to post");
        }
        commentRepository.delete(comment);
    }


    private CommentDto maptoDto(Comment comment){
        CommentDto commentDto=mapper.map(comment,CommentDto.class);
/*        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setBody(comment.getBody());
        commentDto.setEmail(comment.getEmail());*/
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment= mapper.map(commentDto,Comment.class);
/*        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());*/
        return comment;
    }
}
