package org.example.socialse2.controller;

import org.example.socialse2.dto.PostDto;
import org.example.socialse2.dto.UserDto;
import org.example.socialse2.service.PostService;
import org.example.socialse2.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    private final PostService postService;

    private final UserService userService;

    public AdminController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String adminRedirect() {
        return "redirect:/admin/posts";
    }

    @GetMapping("/admin/posts")
    public String getPaginatedAdminPosts(Model model, @RequestParam(defaultValue = "1") int page) {
        int pageSize = 10; // Set your desired page size
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        Page<PostDto> postPage = postService.getPosts(pageable);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        return "admin/posts";
    }

    @GetMapping("/admin/users")
    public String getUsers(@RequestParam(name = "page", defaultValue = "1") int page,
                           @RequestParam(name = "size", defaultValue = "10") int size,
                           Model model) {
        Page<UserDto> userPage = userService.getUsers(page, size);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        return "admin/users";
    }

}
