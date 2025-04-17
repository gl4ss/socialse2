package org.example.socialse2.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.example.socialse2.dto.PostDto;
import org.example.socialse2.mapper.PostMapper;
import org.example.socialse2.model.Post;
import org.example.socialse2.model.User;
import org.example.socialse2.repository.PostRepository;
import org.example.socialse2.repository.UserRepository;
import org.example.socialse2.service.PostService;
import org.example.socialse2.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PostServiceImpl implements PostService {

    private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);
    private final PostRepository contentRepository;
    private final UserRepository accountRepository;

    public PostServiceImpl(PostRepository contentRepository, UserRepository accountRepository) {
        this.contentRepository = contentRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Page<PostDto> retrievePostsPaginated(PageRequest pageRequest) {
        Page<Post> postsPage = contentRepository.findAll(pageRequest);

        log.info("Found {} posts in database with pagination", postsPage.getContent().size());

        return postsPage.map(post -> {
            PostDto dto = PostMapper.toDto(post);

            if (post.getUser() != null) {
                dto.setOwnerName(post.getUser().getUsername());
                dto.setOwnerId(post.getUser().getId());
                log.debug("Post ID {} mapped with owner: {}", post.getId(), post.getUser().getUsername());
            } else {
                log.warn("Post ID {} has no associated user!", post.getId());
            }

            return dto;
        });
    }

    @Override
    @Transactional
    public void createNewPost(PostDto postDto) {
        String username = Objects.requireNonNull(
            SecurityUtils.getCurrentUserDetails(),
            "Authentication required to publish content"
        ).getUsername();

        User account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new EntityNotFoundException("Account not found for authenticated user");
        }

        Post content = PostMapper.toEntity(postDto);
        content.setUser(account);

        if (content.getUpVotes() == null) content.setUpVotes(0L);
        if (content.getDownVotes() == null) content.setDownVotes(0L);

        Post savedPost = contentRepository.save(content);
        log.info("Created new post with ID: {} by user: {}", savedPost.getId(), username);
    }

    @Override
    public PostDto retrievePostById(Long id) {
        return contentRepository.findById(id)
            .map(PostMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException("Content with ID " + id + " not found"));
    }

    @Override
    @Transactional
    public void updateExistingPost(PostDto postDto) {
        Post content = PostMapper.toEntity(postDto);

        User account = accountRepository.findById(postDto.getOwnerId())
            .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + postDto.getOwnerId()));

        content.setUser(account);
        Post updatedPost = contentRepository.save(content);
        log.info("Updated post with ID: {}", updatedPost.getId());
    }

    @Override
    @Transactional
    public void removePost(Long postId) {
        if (!contentRepository.existsById(postId)) {
            throw new EntityNotFoundException("Cannot delete - content with ID " + postId + " not found");
        }
        contentRepository.deleteById(postId);
        log.info("Removed post with ID: {}", postId);
    }

    @Override
    public List<PostDto> searchPostsByTerm(String query) {
        return contentRepository.searchPosts(query).stream()
            .map(PostMapper::toDto)
            .collect(Collectors.toList());
    }
}
