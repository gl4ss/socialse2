package org.example.socialse2.mapper;

import org.example.socialse2.dto.CommentDto;
import org.example.socialse2.model.Comment;

public class CommentMapper {

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                         .id(comment.getId())
                         .content(comment.getContent())
                         .createdAt(comment.getCreatedAt())
                         .updatedAt(comment.getUpdatedAt())
                         .ownerName(comment.getUser().getFirstName() + " " + comment.getUser().getLastName())
                         .build();
    }

    public static Comment toEntity(CommentDto commentDto) {
        return Comment.builder()
                      .id(commentDto.getId())
                      .content(commentDto.getContent())
                      .createdAt(commentDto.getCreatedAt())
                      .updatedAt(commentDto.getUpdatedAt())
                      .build();
    }

}
