package org.example.socialse2.controller;

import jakarta.validation.Valid;
import org.example.socialse2.dto.CommentDto;
import org.example.socialse2.dto.PostDto;
import org.example.socialse2.model.Comment;
import org.example.socialse2.service.CommentService;
import org.example.socialse2.service.PostService;
import org.example.socialse2.service.UserService;
import org.example.socialse2.util.FileUtil;
import org.example.socialse2.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/post")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private static final String ERROR_ACCESS_DENIED = "error/access_denied";
    
    private final PostService contentService;
    private final CommentService feedbackService;
    private final UserService accountService;

    public PostController(PostService contentService, CommentService feedbackService, UserService accountService) {
        this.contentService = contentService;
        this.feedbackService = feedbackService;
        this.accountService = accountService;
    }

    @GetMapping("/create")
    public String showCreateContentForm(Model model) {
        model.addAttribute("postDto", new PostDto());
        return "post/create";
    }

    @PostMapping("/create")
    public String publishNewContent(
            @Valid @ModelAttribute("postDto") PostDto postDto,
            BindingResult bindingResult,
            Model model,
            Principal principal) throws IOException {
            
        if (principal == null) {
            return ERROR_ACCESS_DENIED;
        }
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("postDto", postDto);
            log.error("Validation errors during content creation: {}", bindingResult.getAllErrors());
            return "post/create";
        }
        
        processContentImage(postDto);
        contentService.createNewPost(postDto);
        log.info("New content published successfully: {}", postDto.getTitle());
        
        return redirectBasedOnRole(principal.getName());
    }

    @GetMapping("/{postId}/edit")
    public String showEditContentForm(@PathVariable Long postId, Model model, Principal principal) {
        if (principal == null) {
            return ERROR_ACCESS_DENIED;
        }
        
        String username = principal.getName();
        if (accountService.hasAdminPrivileges(username) || accountService.isPostCreator(username, postId)) {
            model.addAttribute("postDto", contentService.retrievePostById(postId));
            return "post/edit";
        }
        
        return ERROR_ACCESS_DENIED;
    }

    @PostMapping("/{postId}/edit")
    public String updateExistingContent(
            @PathVariable Long postId,
            @Valid @ModelAttribute("postDto") PostDto postDto,
            BindingResult bindingResult,
            Model model,
            Principal principal) throws IOException {
            
        if (principal == null) {
            return ERROR_ACCESS_DENIED;
        }
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("postDto", postDto);
            log.error("Validation errors during content update: {}", bindingResult.getAllErrors());
            return "post/edit";
        }
        
        processContentImage(postDto);
        contentService.updateExistingPost(postDto);
        log.info("Content updated successfully: {}", postDto.getTitle());
        
        return redirectBasedOnRole(principal.getName());
    }

    @GetMapping("/{postId}/delete")
    public String removeContent(@PathVariable Long postId, Principal principal) {
        if (principal == null) {
            return ERROR_ACCESS_DENIED;
        }
        
        String username = principal.getName();
        boolean isAdmin = accountService.hasAdminPrivileges(username);
        boolean isOwner = accountService.isPostCreator(username, postId);
        
        if (isAdmin || isOwner) {
            contentService.removePost(postId);
            log.info("Content with ID {} removed by {}", postId, username);
            return isAdmin ? "redirect:/admin/posts" : "redirect:/";
        }
        
        return ERROR_ACCESS_DENIED;
    }

    @GetMapping("/{postId}")
    public String viewContentDetails(@PathVariable Long postId, Model model) {
        PostDto postDto = contentService.retrievePostById(postId);
        model.addAttribute("postDto", postDto);
        model.addAttribute("postId", postId);
        model.addAttribute("commentDto", new CommentDto());
        model.addAttribute("comments", feedbackService.retrieveCommentsByPostId(postId));
        return "post/view";
    }

    @PostMapping("/{postId}/comment")
    public String submitFeedback(
            @PathVariable Long postId,
            @ModelAttribute("commentDto") CommentDto commentDto,
            Principal principal) {
            
        if (principal == null) {
            return ERROR_ACCESS_DENIED;
        }
        
        feedbackService.createNewComment(postId, commentDto);
        log.info("Feedback added to content ID {} by {}", postId, principal.getName());
        return "redirect:/post/{postId}";
    }

    @GetMapping("/{postId}/comment/{commentId}/delete")
    public String removeFeedback(
            @PathVariable Long postId, 
            @PathVariable Long commentId, 
            Principal principal) {
            
        if (principal == null) {
            return ERROR_ACCESS_DENIED;
        }
        
        Comment comment = feedbackService.retrieveCommentById(commentId);
        String commentOwner = comment.getUser().getUsername();
        
        if (!commentOwner.equals(principal.getName()) && !accountService.hasAdminPrivileges(principal.getName())) {
            return ERROR_ACCESS_DENIED;
        }
        
        feedbackService.removeComment(commentId);
        log.info("Feedback ID {} removed from content ID {} by {}", commentId, postId, principal.getName());
        return "redirect:/post/" + postId;
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> retrieveContentImage(@PathVariable Long id) {
        PostDto postDto = contentService.retrievePostById(id);
        byte[] image = postDto.getImage();
        
        if (image == null || image.length == 0) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(image);
    }
    
    // Helper methods
    private void processContentImage(PostDto postDto) throws IOException {
        if (postDto.getImageFile() != null && !postDto.getImageFile().isEmpty()) {
            postDto.setImage(postDto.getImageFile().getBytes());
        }
    }
    
    private String redirectBasedOnRole(String username) {
        return accountService.hasAdminPrivileges(username) ? "redirect:/admin/posts" : "redirect:/";
    }
}
