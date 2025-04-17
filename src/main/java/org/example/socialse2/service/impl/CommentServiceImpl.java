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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository feedbackRepository;
    private final PostRepository contentRepository;
    private final UserRepository accountRepository;

    public CommentServiceImpl(CommentRepository feedbackRepository,
                              PostRepository contentRepository,
                              UserRepository accountRepository) {
        this.feedbackRepository = feedbackRepository;
        this.contentRepository = contentRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public void createNewComment(Long postId, CommentDto commentDto) {
        String username = Objects.requireNonNull(
            SecurityUtils.getCurrentUserDetails(), 
            "Authentication required to leave feedback"
        ).getUsername();
        
        User account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new EntityNotFoundException("Account not found for authenticated user");
        }
        
        Post content = contentRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException("Content with ID " + postId + " not available"));
            
        Comment feedback = CommentMapper.toEntity(commentDto);
        feedback.setPost(content);
        feedback.setUser(account);
        feedbackRepository.save(feedback);
    }

    @Override
    public List<CommentDto> retrieveCommentsByPostId(Long postId) {
        Post content = contentRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException("Content with ID " + postId + " not available"));
            
        return feedbackRepository.findByPost(content).stream()
            .map(CommentMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeComment(Long commentId) {
        if (!feedbackRepository.existsById(commentId)) {
            throw new EntityNotFoundException("Cannot delete - feedback with ID " + commentId + " not found");
        }
        feedbackRepository.deleteById(commentId);
    }

    @Override
    public Comment retrieveCommentById(Long commentId) {
        return feedbackRepository.findById(commentId)
            .orElseThrow(() -> new EntityNotFoundException("Feedback with ID " + commentId + " not found"));
    }
}
