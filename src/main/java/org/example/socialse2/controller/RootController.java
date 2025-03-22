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

import java.util.List;

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
        int pageSize = 10; // Set your desired page size
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        Page<PostDto> postPage = postService.getPosts(pageable);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "query") String query, Model model) {
        List<UserDto> userDtoList = userService.searchUsers(query);
        List<PostDto> postDtoList = postService.searchPosts(query);
        model.addAttribute("users", userDtoList);
        model.addAttribute("posts", postDtoList);
        return "search";
    }

}
