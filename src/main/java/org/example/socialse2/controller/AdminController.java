package org.example.socialse2.controller;

import org.example.socialse2.dto.PostDto;
import org.example.socialse2.dto.UserDto;
import org.example.socialse2.service.PostService;
import org.example.socialse2.service.UserService;
import org.example.socialse2.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PostService postService;
    private final UserService userService;

    public AdminController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public String adminRedirect() {
        return "redirect:/admin/posts";
    }

    @GetMapping("/posts")
    public String getPaginatedAdminPosts(
            Model model, 
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
            
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<PostDto> postPage = postService.getPosts(pageable);
        
        PaginationUtil.addPaginationAttributes(model, postPage, page);
        return "admin/posts";
    }

    @GetMapping("/users")
    public String getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
            
        Page<UserDto> userPage = userService.getUsers(page, size);
        PaginationUtil.addPaginationAttributes(model, userPage, page);
        return "admin/users";
    }
}
