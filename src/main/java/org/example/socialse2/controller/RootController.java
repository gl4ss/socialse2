package org.example.socialse2.controller;

import org.example.socialse2.dto.CommentDto;
import org.example.socialse2.dto.PostDto;
import org.example.socialse2.dto.UserDto;
import org.example.socialse2.service.CommentService;
import org.example.socialse2.service.PostService;
import org.example.socialse2.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RootController {

    private static final Logger log = LoggerFactory.getLogger(RootController.class);
    private final PostService contentService;
    private final UserService accountService;
    private final CommentService commentService;

    public RootController(PostService contentService, UserService accountService, CommentService commentService) {
        this.contentService = contentService;
        this.accountService = accountService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public String displayHomepage(Model model, @RequestParam(defaultValue = "1") int page) {
        int pageSize = 10; // Set your desired page size

        // Sort by creation date descending to show newest posts first
        PageRequest pageable = PageRequest.of(page - 1, pageSize, Sort.by("createdAt").descending());
        Page<PostDto> contentPage = contentService.retrievePostsPaginated(pageable);

        // Enhance posts with newest comment
        List<PostDto> enhancedPosts = contentPage.getContent().stream()
            .map(post -> {
                // Get comments for this post
                List<CommentDto> comments = commentService.retrieveCommentsByPostId(post.getId());
                
                // Set the newest comment (if any exists)
                if (comments != null && !comments.isEmpty()) {
                    // Sort comments by creation time (newest first)
                    comments.sort(Comparator.comparing(CommentDto::getCreatedAt).reversed());
                    // Set the newest comment
                    post.setNewestComment(comments.get(0));
                }
                
                return post;
            })
            .collect(Collectors.toList());

        // Log post information for debugging
        log.info("Retrieved {} posts for homepage", enhancedPosts.size());

        model.addAttribute("posts", enhancedPosts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contentPage.getTotalPages());

        return "index";
    }

    @GetMapping("/search")
    public String performSearch(@RequestParam(value = "query") String searchTerm, Model model) {
        List<UserDto> accountResults = accountService.findUsersBySearchTerm(searchTerm);
        List<PostDto> contentResults = contentService.searchPostsByTerm(searchTerm);

        model.addAttribute("users", accountResults);
        model.addAttribute("posts", contentResults);
        model.addAttribute("searchTerm", searchTerm);

        return "search";
    }
}
