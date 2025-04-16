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

    private final PostService contentService;
    private final UserService accountService;

    public AdminController(PostService contentService, UserService accountService) {
        this.contentService = contentService;
        this.accountService = accountService;
    }

    @GetMapping("")
    public String redirectToAdminDashboard() {
        return "redirect:/admin/posts";
    }

    @GetMapping("/posts")
    public String manageContentPaginated(Model model, @RequestParam(defaultValue = "1") int page) {
        // Ensure page is at least 1
        int pageNumber = Math.max(1, page);
        int pageSize = 10;
        
        // Convert from 1-based (user-friendly) to 0-based (Spring Data)
        PageRequest pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<PostDto> contentPage = contentService.retrievePostsPaginated(pageable);
        
        model.addAttribute("posts", contentPage.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", contentPage.getTotalPages());
        
        return "admin/posts";
    }

    @GetMapping("/users")
    public String manageUsersPaginated(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {
            
        // Ensure page is at least 1 and size is reasonable
        int pageNumber = Math.max(1, page);
        int pageSize = Math.min(100, Math.max(1, size)); // Between 1 and 100
        
        // We need to pass the correct parameters to match the service implementation
        Page<UserDto> accountsPage = accountService.retrieveUsersPaginated(pageNumber, pageSize);
        
        model.addAttribute("users", accountsPage.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", accountsPage.getTotalPages());
        
        return "admin/users";
    }
}
