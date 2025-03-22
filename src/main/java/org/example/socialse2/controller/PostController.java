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
    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    public PostController(PostService postService, CommentService commentService, UserService userService) {
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String newPostForm(Model model) {
        PostDto postDto = new PostDto();
        model.addAttribute("postDto", postDto);
        return "post/create";
    }

    @PostMapping("/create")
    public String createPost(
            @Valid @ModelAttribute("postDto") PostDto postDto,
            BindingResult bindingResult,
            Model model,
            Principal principal) throws IOException {
            
        if (SecurityUtil.isNotAuthenticated(principal)) {
            return "error/access_denied";
        }
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("postDto", postDto);
            log.error(bindingResult.getAllErrors().toString());
            return "post/create";
        }
        
        FileUtil.processPostImage(postDto);
        postService.createPost(postDto);
        log.info("Post created: {}", postDto);
        
        return userService.isAdmin(principal.getName()) ? "redirect:/admin/posts" : "redirect:/";
    }

    @GetMapping("/{postId}/edit")
    public String editPostForm(@PathVariable Long postId, Model model, Principal principal) {
        if (SecurityUtil.isNotAuthenticated(principal) || 
            !hasAccessToPost(principal.getName(), postId)) {
            return "error/access_denied";
        }
        
        PostDto postDto = postService.getPost(postId);
        model.addAttribute("postDto", postDto);
        return "post/edit";
    }

    @PostMapping("/{postId}/edit")
    public String editPost(
            @PathVariable Long postId,
            @Valid @ModelAttribute("postDto") PostDto postDto,
            BindingResult bindingResult,
            Model model,
            Principal principal) throws IOException {
            
        if (SecurityUtil.isNotAuthenticated(principal) || 
            !hasAccessToPost(principal.getName(), postId)) {
            return "error/access_denied";
        }
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("postDto", postDto);
            log.error(bindingResult.getAllErrors().toString());
            return "post/edit";
        }
        
        FileUtil.processPostImage(postDto);
        postService.updatePost(postDto);
        log.info("Post updated: {}", postDto);
        
        return userService.isAdmin(principal.getName()) ? "redirect:/admin/posts" : "redirect:/";
    }

    @GetMapping("/{postId}/delete")
    public String deletePost(@PathVariable Long postId, Principal principal) {
        if (SecurityUtil.isNotAuthenticated(principal) || 
            !hasAccessToPost(principal.getName(), postId)) {
            return "error/access_denied";
        }
        
        postService.deletePost(postId);
        boolean isAdmin = userService.isAdmin(principal.getName());
        return isAdmin ? "redirect:/admin/posts" : "redirect:/";
    }

    @GetMapping("/{postId}")
    public String viewPost(@PathVariable Long postId, Model model) {
        PostDto postDto = postService.getPost(postId);
        model.addAttribute("postDto", postDto);
        model.addAttribute("commentDto", new CommentDto());
        model.addAttribute("comments", commentService.getComments(postId));
        return "post/view";
    }

    @PostMapping("/{postId}/comment")
    public String addComment(
            @PathVariable Long postId,
            @ModelAttribute("commentDto") CommentDto commentDto,
            Principal principal) {
            
        if (SecurityUtil.isNotAuthenticated(principal)) {
            return "error/access_denied";
        }
        
        commentService.addComment(postId, commentDto);
        return "redirect:/post/{postId}";
    }

    @GetMapping("/{postId}/comment/{commentId}/delete")
    public String deleteComment(
            @PathVariable Long postId, 
            @PathVariable Long commentId, 
            Principal principal) {
            
        if (SecurityUtil.isNotAuthenticated(principal)) {
            return "error/access_denied";
        }
        
        Comment comment = commentService.getById(commentId);
        String commentOwner = comment.getUser().getUsername();
        
        if (!commentOwner.equals(principal.getName()) && 
            !userService.isAdmin(principal.getName())) {
            return "error/access_denied";
        }
        
        commentService.deleteComment(commentId);
        return "redirect:/post/" + postId;
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        PostDto postDto = postService.getPost(id);
        byte[] image = postDto.getImage();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
            .body(image);
    }
    
    // Helper method to check if user has access to a post
    private boolean hasAccessToPost(String username, Long postId) {
        return userService.isAdmin(username) || userService.isOwnerOfPost(username, postId);
    }
}
