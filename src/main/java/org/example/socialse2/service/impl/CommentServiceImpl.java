package org.example.socialse2.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.socialse2.dto.CommentDto;
import org.example.socialse2.mapper.CommentMapper;
import org.example.socialse2.model.Comment;
import org.example.socialse2.model.Post;
import org.example.socialse2.model.User;
import org.example.socialse2.repository.CommentRepository;
import org.example.socialse2.repository.PostRepository;
import org.example.socialse2.repository.UserRepository;
import org.example.socialse2.service.CommentService;
import org.example.socialse2.util.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository,
                              UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addComment(Long postId, CommentDto commentDto) {
        String username = Objects.requireNonNull(SecurityUtils.getCurrentUserDetails()).getUsername();
        User user = userRepository.findByUsername(username);
        postRepository.findById(postId).ifPresent(post -> {
            Comment comment = CommentMapper.toEntity(commentDto);
            comment.setPost(post);
            comment.setUser(user);
            commentRepository.save(comment);
        });
    }

    @Override
    public List<CommentDto> getComments(Long postId) {
        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new EntityNotFoundException("Post with id " +
                                                                                 postId +
                                                                                 " not found"));
        return commentRepository.findByPost(post).stream().map(CommentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment getById(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            return comment.get();
        }
        throw new EntityNotFoundException("Comment with id " + commentId + " not found");
    }

}
