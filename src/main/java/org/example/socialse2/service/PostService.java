package org.example.socialse2.service;

import org.example.socialse2.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface PostService {

    Page<PostDto> getPosts(PageRequest pageRequest);

    void createPost(PostDto post);

    PostDto getPost(Long id);

    void updatePost(PostDto postDto);

    void deletePost(Long postId);

    List<PostDto> searchPosts(String query);

}
