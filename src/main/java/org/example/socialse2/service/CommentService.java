package org.example.socialse2.service;

import org.example.socialse2.dto.CommentDto;
import org.example.socialse2.model.Comment;

import java.util.List;

public interface CommentService {

    void createNewComment(Long postId, CommentDto commentDto);

    List<CommentDto> retrieveCommentsByPostId(Long postId);

    void removeComment(Long commentId);

    Comment retrieveCommentById(Long commentId);

}
