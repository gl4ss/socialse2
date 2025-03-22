package org.example.socialse2.mapper;

import org.example.socialse2.dto.PostDto;
import org.example.socialse2.model.Post;

public class PostMapper {

    public static PostDto toDto(Post post) {
        return PostDto.builder()
                      .id(post.getId())
                      .title(post.getTitle())
                      .content(post.getContent())
                      .image(post.getImage())
                      .createdAt(post.getCreatedAt())
                      .updatedAt(post.getUpdatedAt())
                      .upVotes(post.getUpVotes())
                      .downVotes(post.getDownVotes())
                      .ownerName(post.getUser().getUsername())
                      .ownerId(post.getUser().getId())
                      .shortDescription(post.getShortDescription())
                      .build();
    }

    public static Post toEntity(PostDto postDto) {
        return Post.builder()
                   .id(postDto.getId())
                   .title(postDto.getTitle())
                   .content(postDto.getContent())
                   .image(postDto.getImage())
                   .createdAt(postDto.getCreatedAt())
                   .updatedAt(postDto.getUpdatedAt())
                   .upVotes(postDto.getUpVotes())
                   .downVotes(postDto.getDownVotes())
                   .shortDescription(postDto.getShortDescription())
                   .build();
    }

}
