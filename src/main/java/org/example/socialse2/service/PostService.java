package org.example.socialse2.service;

import org.example.socialse2.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface PostService {

    Page<PostDto> retrievePostsPaginated(PageRequest pageRequest);

    void createNewPost(PostDto post);

    PostDto retrievePostById(Long id);

    void updateExistingPost(PostDto postDto);

    void removePost(Long postId);

    List<PostDto> searchPostsByTerm(String query);

}
