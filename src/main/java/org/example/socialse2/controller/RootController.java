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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RootController {

    private final PostService postService;
    private final UserService userService;

    public RootController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model, @RequestParam(defaultValue = "1") int page) {
        int pageSize = 10;
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        Page<PostDto> postPage = postService.getPosts(pageable);
        
        PaginationUtil.addPaginationAttributes(model, postPage, page);
        return "index";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(value = "query") String query,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        
        int pageSize = 10;
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        
        Page<UserDto> userPage = userService.searchUsers(query, pageable);
        Page<PostDto> postPage = postService.searchPosts(query, pageable);
        
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", Math.max(userPage.getTotalPages(), postPage.getTotalPages()));
        model.addAttribute("query", query);
        
        return "search";
    }
}
