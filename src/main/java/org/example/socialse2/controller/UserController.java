package org.example.socialse2.controller;

import jakarta.validation.Valid;
import org.example.socialse2.dto.PostDto;
import org.example.socialse2.dto.UserDto;
import org.example.socialse2.mapper.PostMapper;
import org.example.socialse2.mapper.UserMapper;
import org.example.socialse2.model.Post;
import org.example.socialse2.model.User;
import org.example.socialse2.service.CommentService;
import org.example.socialse2.service.PostService;
import org.example.socialse2.service.UserService;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final String ERROR_ACCESS_DENIED = "error/access_denied";

    private final UserService accountService;
    private final PostService postService;
    private final CommentService commentService;

    public UserController(UserService accountService, PostService postService, CommentService commentService) {
        this.accountService = accountService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping
    public String displayProfileForCurrentUser(Principal principal) {
        if (principal == null) {
            return ERROR_ACCESS_DENIED;
        }
        User currentUser = accountService.retrieveCurrentAuthenticatedUser();
        return "redirect:/user/" + currentUser.getId();
    }

    @GetMapping("/{id}")
    public String displayUserProfile(@PathVariable("id") Long id, Model model) {
        User accountDetails = accountService.retrieveUserById(id);
        model.addAttribute("user", accountDetails);
        
        // Get recent posts by this user (last 5)
        List<PostDto> recentPosts = new ArrayList<>();
        
        if (accountDetails.getPosts() != null && !accountDetails.getPosts().isEmpty()) {
            // Get the 5 most recent posts for this user
            recentPosts = accountDetails.getPosts().stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .limit(5)
                .map(post -> {
                    PostDto dto = PostMapper.toDto(post);
                    
                    // Get the comment count for this post
                    List<?> comments = commentService.retrieveCommentsByPostId(post.getId());
                    dto.setCommentCount((long) (comments != null ? comments.size() : 0));
                    
                    return dto;
                })
                .collect(Collectors.toList());
        }
        
        model.addAttribute("recentPosts", recentPosts);
        return "user/profile";
    }

    @GetMapping("/edit")
    public String displayProfileEditForm(Model model, Principal principal) {
        if (principal == null) {
            return ERROR_ACCESS_DENIED;
        }
        
        User accountDetails = accountService.retrieveCurrentAuthenticatedUser();
        UserDto accountDto = UserMapper.toDto(accountDetails);
        model.addAttribute("user", accountDto);
        return "user/edit";
    }

    @PostMapping("/edit")
    public String modifyAccountDetails(
            @Valid @ModelAttribute("user") UserDto userDto,
            BindingResult bindingResult,
            Model model,
            Principal principal) throws IOException {
            
        if (principal == null) {
            return ERROR_ACCESS_DENIED;
        }
        
        if (bindingResult.hasErrors()) {
            log.error("Validation errors during profile modification: {}", bindingResult.getAllErrors());
            model.addAttribute("userDto", userDto);
            return "user/edit";
        }
        
        User currentUser = accountService.retrieveCurrentAuthenticatedUser();
        if (!currentUser.getId().equals(userDto.getId())) {
            log.warn("Account {} attempted to modify profile of account {}", 
                    currentUser.getUsername(), userDto.getUsername());
            return ERROR_ACCESS_DENIED;
        }
        
        processProfileImage(userDto);
        accountService.modifyUserProfile(userDto);
        log.info("Account details updated successfully for: {}", userDto.getUsername());
        
        return "redirect:/user/" + currentUser.getId();
    }

    @GetMapping("/{id}/delete")
    public String deactivateAccount(Principal principal, @PathVariable Long id) {
        if (principal == null) {
            return ERROR_ACCESS_DENIED;
        }
        
        User currentUser = accountService.retrieveCurrentAuthenticatedUser();
        boolean isAdmin = accountService.hasAdminPrivileges(currentUser.getUsername());
        boolean isSelfDelete = currentUser.getId().equals(id);
        
        if (!isAdmin && !isSelfDelete) {
            log.warn("Unauthorized attempt to deactivate account {} by {}", id, currentUser.getUsername());
            return ERROR_ACCESS_DENIED;
        }
        
        accountService.removeUserAccount(id);
        log.info("Account with ID {} deactivated by {}", id, currentUser.getUsername());
        
        // Redirect to admin panel if admin is deactivating someone else's account
        if (isAdmin && !isSelfDelete) {
            return "redirect:/admin/users";
        }
        
        // Redirect to logout if user deactivated their own account
        return "redirect:/logout";
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> retrieveProfilePicture(@PathVariable Long id) {
        User user = accountService.retrieveUserById(id);
        byte[] image = user.getProfileImage();
        
        if (image == null || image.length == 0) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(image);
    }
    
    // Helper methods
    private void processProfileImage(UserDto userDto) throws IOException {
        if (userDto.getImageFile() != null && !userDto.getImageFile().isEmpty()) {
            userDto.setProfileImage(userDto.getImageFile().getBytes());
            log.debug("Profile picture processed for account: {}", userDto.getUsername());
        }
    }
}
